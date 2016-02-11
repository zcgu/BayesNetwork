package myPackage;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gu on 2016/2/10.
 */
public class DataSetLoaderWithWeka {

    private DataSet trainData;
    private DataSet testData;



    public DataSetLoaderWithWeka(String path1, String path2) throws IOException {
        trainData = loadData(path1);
        testData = loadData(path2);
    }

    public DataSet getTrainData() { return trainData; }

    public DataSet getTestData() { return testData; }

    private DataSet loadData(String path) throws IOException {

        // Read file to Instances data.
        BufferedReader br = new BufferedReader(new FileReader(path));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(br, 100000);
        Instances data = arff.getStructure();
        data.setClassIndex(data.numAttributes() - 1);
        Instance inst;
        while ((inst = arff.readInstance(data)) != null) {
            data.add(inst);
        }

        // Read Instances to Dataset.
        DataSet dataSet = new DataSet();
        dataSet.relation = data.relationName();

        // Read the attributes.
        for(int i=0; i<data.numAttributes() - 1; i++ ) {
            DataSet.Attribute myAttribute = new DataSet.Attribute();
            Attribute attribute = data.attribute(i);

            myAttribute.name = attribute.name();
            for(int j=0; j<attribute.numValues(); j++) myAttribute.possibleValues.add(attribute.value(j));
            dataSet.attributeList.add(myAttribute);
        }
        // Class Attribute.
        DataSet.Attribute myAttribute = new DataSet.Attribute();
        Attribute attribute = data.classAttribute();

        myAttribute.name = attribute.name();
        for(int j=0; j<attribute.numValues(); j++) myAttribute.possibleValues.add(attribute.value(j));
        dataSet.classLabel = myAttribute;


        // Read data.
        for(int i=0; i<data.numInstances(); i++) {
            Instance instance = data.instance(i);
            Map<DataSet.Attribute, String > map = new HashMap<>();

            for (int j=0; j<data.numAttributes() - 1; j++)
            {
                map.put(dataSet.attributeList.get(j), instance.toString(data.attribute(j)));
            }
            map.put(dataSet.classLabel, instance.toString(data.classAttribute()));

            dataSet.data.add(map);
        }

        return dataSet;
    }

}
