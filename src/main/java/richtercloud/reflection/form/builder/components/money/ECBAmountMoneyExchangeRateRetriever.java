/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.reflection.form.builder.components.money;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jscience.economics.money.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author richter
 */
public class ECBAmountMoneyExchangeRateRetriever extends CachedOnlineAmountMoneyExchangeRateRetriever {
    private final static Logger LOGGER = LoggerFactory.getLogger(ECBAmountMoneyExchangeRateRetriever.class);
    private final static String ECB_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private final static Currency REFERENCE_CURRENCY = Currency.EUR;
    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    private Map<Currency, Double> retrieveResponse() throws AmountMoneyExchangeRateRetrieverException {
        Map<Currency, Double> retValue = new HashMap<>();
        try {
            URLConnection uRLConnection = new URL(ECB_URL).openConnection();
            InputStream inputStream = uRLConnection.getInputStream();
            String responseXMLString = IOUtils.toString(inputStream);
            LOGGER.debug(String.format("%s replied: %s", ECB_URL, responseXMLString));
            //It's fine to build the tree/load everything into memory since the
            //response is quite small
            DocumentBuilder builder  = factory.newDocumentBuilder();
            Document        document = builder.parse(new InputSource(new StringReader(responseXMLString)));
            Node            rootNode = document.getDocumentElement();
            NodeList rootNodeChildren = rootNode.getChildNodes();
            Node cubeNode = null;
            for(int i=0; i<rootNodeChildren.getLength(); i++) {
                if("Cube".equals(rootNodeChildren.item(i).getNodeName())) {
                    cubeNode = rootNodeChildren.item(i);
                }
            }
            if(cubeNode == null) {
                throw new IllegalStateException("XML response didn't contain "
                        + "a Cube element");
            }
            Node cubeCubeNode = null;
            NodeList cubeNodeChildren = cubeNode.getChildNodes();
            for(int i=0; i<cubeNodeChildren.getLength(); i++) {
                if("Cube".equals(cubeNodeChildren.item(i).getNodeName())) {
                    cubeCubeNode = cubeNodeChildren.item(i);
                }
            }
            if(cubeCubeNode == null) {
                throw new IllegalStateException("XML response didn't contain "
                        + "a Cube element in the topmost Cube element");
            }
            NodeList cubeCubeNodeChildren = cubeCubeNode.getChildNodes();
            for(int i=0; i<cubeCubeNodeChildren.getLength(); i++) {
                Node cubeCubeNodeChild = cubeCubeNodeChildren.item(i);
                if(!"Cube".equals(cubeCubeNodeChild.getNodeName())) {
                    //There're #text and DeferredElement and other things around
                    continue;
                }
                Node cubeCubeNodeChildCurrencyAttribute = cubeCubeNodeChild.getAttributes().getNamedItem("currency");
                if(cubeCubeNodeChildCurrencyAttribute == null) {
                    throw new IllegalStateException("XML response contains "
                            + "Cube element without currency attribute");
                }
                Node cubeCubeNodeChildRateAttribute = cubeCubeNodeChild.getAttributes().getNamedItem("rate");
                String currencyCode = cubeCubeNodeChildCurrencyAttribute.getNodeValue();
                Double rate = Double.valueOf(cubeCubeNodeChildRateAttribute.getNodeValue());
                Currency currency = new Currency(currencyCode);
                retValue.put(currency, rate);
            }
            return retValue;
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            throw new AmountMoneyExchangeRateRetrieverException(ex);
        }
    }

    @Override
    protected Pair<Map<Currency, Double>, Currency> fetchResult() throws AmountMoneyExchangeRateRetrieverException {
        Map<Currency, Double> response = retrieveResponse();
        response.put(REFERENCE_CURRENCY,
                1.0);
        return new ImmutablePair<>(response,
                REFERENCE_CURRENCY);
    }

    @Override
    protected String getUrl() {
        return ECB_URL;
    }
}
