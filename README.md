# reflection-form-builder
`reflection-form-builder` is a framework which allows to create GUI forms for Java (Swing) for Java classes automatically based on their properties (a `double` field will become a `JSpinner`, a `String` will become a `JTextField`, a `List<Integer>` will become a component which lets you add, edit and delete a list of integers - you get the idea...).

References to other classes are supported in the [`reflection-form-builder-jpa` project](https://github.com/document-scanner/reflection-form-builder-jpa) by allowing queries based on pre-configured as well as free query texts which reduces the need for the user to know how to query a database with a text interface to a minimum.

The demo is available in the [`reflection-form-builder-demo` project](https://github.com/document-scanner/reflection-form-builder-demo) (see build instructions below on how to get started). Open the project in an IDE which supports the Apache Maven build tool (probably all will). All `main` classes are demo classes which you can run.

## Building
Use the [Apache Maven aggregator project `reflection-form-builder-aggregator`](https://github.com/document-scanner/reflection-form-builder-aggregator) in order to build all subprojects properly.

## Used by
  * [`document-scanner`](https://github.com/document-scanner/document-scanner), a document scan assistant and manager

## License
`document-scanner` is free software - [free as in "free speech" - not as in "free beer"](https://www.gnu.org/philosophy/free-sw.html) licensed under the [GNU General Public License version 3 (GPLv3)](https://en.wikipedia.org/wiki/GNU_General_Public_License#Version_3).
