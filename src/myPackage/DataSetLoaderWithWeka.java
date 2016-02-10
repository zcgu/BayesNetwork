package myPackage;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by gu on 2016/2/10.
 */
public class DataSetLoaderWithWeka {

    public DataSetLoaderWithWeka(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(br, 10000);
        Instances data = arff.getStructure();
        data.setClassIndex(data.numAttributes() - 1);
        Instance inst;
        while ((inst = arff.readInstance(data)) != null) {
            data.add(inst);
        }
    }
}
