
TARGET_DIR=/opt/freerouter

mkdir -p $TARGET_DIR/bin
cp fr.jar $TARGET_DIR/bin

cp run.sh $TARGET_DIR
cp runp.sh $TARGET_DIR

chmod o+x $TARGET_DIR/run.sh
chmod o+x $TARGET_DIR/runp.sh


echo "installed in" $TARGET_DIR
