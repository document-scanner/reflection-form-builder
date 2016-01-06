/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.reflection.form.builder.panels;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 * Displays the same component as {@link EntityListPanelCellRenderer}.
 *
 * Editing doesn't have any effect.
 * @author richter
 */
public class SimpleEntityListPanelCellEditor extends ListPanelTableCellEditor<JLabel> {
    private static final long serialVersionUID = 1L;

    public SimpleEntityListPanelCellEditor() {
        super(new JLabel());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        getComponent().setText(value != null ? value.toString() : null);
        Component retValue = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        return retValue;


        /*JComponent newElement = null;
        for(Pair<Class<? extends Annotation>, ClassAnnotationHandler> pair : this.getReflectionFormBuilder().getClassAnnotationMapping()) {
            if(((Class<?>)this.entityClass).getAnnotation(pair.getKey()) != null) {
                try {
                    newElement = pair.getValue().handle(this.getReflectionFormBuilder().retrieveRelevantFields((Class<?>) this.entityClass), (Class<?>) this.entityClass);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        final int index = this.getMainListModel().getRowCount();
        if(newElement == null) {
            FieldHandler fieldHandler = this.getReflectionFormBuilder().retrieveFieldHandler(this.entityClass);
            if(fieldHandler == null) {
                throw new IllegalArgumentException(String.format("no class annotation and no %s is mapped to type %s in the associated %s", FieldHandler.class, this.entityClass, ReflectionFormBuilder.class));
            }
//            //create entity
//            Object entity;
//            Constructor<?> entityClassConstructor;
//            if(this.listGenericType instanceof Class) {
//                try {
//                    entityClassConstructor = ((Class)this.listGenericType).getDeclaredConstructor();
//                } catch (NoSuchMethodException | SecurityException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }else if(this.listGenericType instanceof ParameterizedType) {
//                try {
//                    entityClassConstructor = ((Class)(((ParameterizedType)this.listGenericType).getRawType())).getDeclaredConstructor();
//                } catch (NoSuchMethodException | SecurityException ex) {
//                    throw new RuntimeException(ex);
//                }
//            } else {
//                throw new IllegalArgumentException(String.format("listGenericType %s has to be either a %s or a %s", this.listGenericType, Class.class, ParameterizedType.class));
//            }
//            entityClassConstructor.setAccessible(true);
//            try {
//                entity = entityClassConstructor.newInstance();
//            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
//                throw new RuntimeException(ex);
//            }
            newElement = fieldHandler.handle(this.entityClass, new UpdateListener() {
                @Override
                public void onUpdate(UpdateEvent event) {
                    for(ListPanelItemListener listener : SimpleEntityListPanel.this.getItemListeners()) {
                        listener.onItemAdded(new ListPanelItemEvent(ListPanelItemEvent.EVENT_TYPE_CHANGED, index, SimpleEntityListPanel.this.getMainListModel().getData()));
                    }
                }
            }, this.getReflectionFormBuilder());
        }
        return newElement;*/
    }

    /**
     * editing doesn't have any effect
     * @return
     */
    @Override
    protected Object stopCellEditing0() {
        return this.getComponent().getText();
    }

}
