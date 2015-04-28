#build deps
#deb's: javahelp2 icedtea-netx-common

ADD_JABA_CLASSPATH=/usr/share/java/jh.jar:/usr/share/icedtea-web/netx.jar

JABA_FLIST=`find -type f -name "*.java"`


javac  -classpath $ADD_JABA_CLASSPATH $JABA_FLIST

PROP_LIST=`find -type f \( -name "*.class" -o -name "*.properties" \)`

jar cfe fr.jar gui.MainApplication $PROP_LIST
