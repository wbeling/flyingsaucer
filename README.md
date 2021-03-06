TODO
----
+ Support MS Word output.
+ Make cancelable/interrupted support.
+ Make case insensitive and locale insensitive.
+ Gradients.
+ Media queries.
+ Replace StringBuffer with StringBuilder.
+ Remove redundant casts.
+ Update LICENSE.
+ Update Samples.
+ Support media rules.
+ Support Bootstrap styles.
+ Lots more tests.
+ Implement builder API. See [proposed interface](/flying-saucer-core/src/main/java/org/xhtmlrenderer/service/HtmlRenderServiceConfigBuilder.java).

DONE
----
+ Replace XML parser with Jsoup HTML5 parser.
+ Use a better text-breaker (Java's BreakIterator) to handle more languages.
+ CSS3 Support
  + border-radius (J2D, PDF)
  + linear-gradient (J2D)
  + opacity (J2D)
  + rgba (J2D)
+ Use external libraries for non-core functionality such as commons codec for base64 and Scalr for image scaling.
+ Move to Java 1.7 including use of generic types, enums, etc.
+ Remove SWT, itext5 and docbook support.
+ Move to latest dependencies.
+ Delete extra files, jars, etc.



Flying Saucer
-------------

http://code.google.com/p/flying-saucer/
Please see project website for links to git, mailing lists, issue tracker, etc.


OVERVIEW
--------
Flying Saucer is a pure-Java library for rendering arbitrary well-formed XML 
(or XHTML) using CSS 2.1 for layout and formatting, output to Swing panels, 
PDF, and images.

Comprehensive documentation available in our user's guide, linked from our website at http://code.google.com/p/flying-saucer/
For information on our development releases, please contact us on our mailing lists.

If you end up using Flying Saucer for your own projects, please drop us an
email and tell us about it; it helps inform where we go next, and is interesting
and inspiring to other developers.


LICENSE
-------
Flying Saucer is distributed under the LGPL.  Flying Saucer itself is licensed 
under the GNU Lesser General Public License, version 2.1 or later, available at
http://www.gnu.org/copyleft/lesser.html. You can use Flying Saucer in any
way and for any purpose you want as long as you respect the terms of the 
license. A copy of the LGPL license is included as license-lgpl-2.1.txt
in our distributions and in our source tree.

Flying Saucer uses a couple of FOSS packages to get the job done. A list
of these, along with the license they each have, is listed in the 
LICENSE file in our distribution.   

GETTING FLYING SAUCER
---------------------
New releases of Flying Saucer are distributed through Maven.  The available artifacts are:

+ org.xhtmlrenderer:flying-saucer-core - Core library and Java2D rendering
+ org.xhtmlrenderer:flying-saucer-pdf - PDF output using iText
+ org.xhtmlrenderer:flying-saucer-swt - SWT output
+ org.xhtmlrenderer:flying-saucer-log4j - Logging plugin for log4j

GETTING STARTED
---------------
There is a large amount of sample code under the flying-saucer-demos directory.
A pre-configured Eclipse project is provided to run a few of them.  

For users of other IDEs, make sure the src/java and resources directories under
the about, docbook, svg, and browser directories are available on the build
path.  samples/src and splash should also be in the build path.

flying-saucer-core, flying-saucer-pdf, and flying-saucer-swt must also be on the
build path as well as the an SWT JAR for your OS.

The Eclipse config files as committed with flying-saucer-swt use a Linux SWT
JAR.  You will need to update if using another OS.

org.xhtmlrenderer.demo.browser.BrowserStartup will start the browser demo.

Some good entry points (classes) are:
+ org.xhtmlrenderer.simple.XHTMLPanel
+ org.xhtmlrenderer.simple.PDFRenderer
+ org.xhtmlrenderer.simple.ImageRenderer

