package org.apache.mahout.classifier.pg.mapreduce;

import com.google.common.base.Preconditions;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.classifier.pg.builder.PGgenerator;
import org.apache.mahout.classifier.pg.data.Dataset;
import org.apache.mahout.keel.Dataset.InstanceSet;

import java.io.IOException;

/**
 * Base class for Mapred mappers. Loads common parameters from the job
 */
public class MapredMapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT> extends Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT> {
  
  private boolean noOutput;
  
  protected PGgenerator pg_algorithm;
  
  protected String header;
  protected int windows;
  private Dataset dataset;
  
  /**
   * 
   * @return if false, the mapper does not estimate and output predictions
   */
  protected boolean isNoOutput() {
    return noOutput;
  }
  
  protected PGgenerator getPGgeneratorBuilder() {
    return pg_algorithm;
  }
  
  protected Dataset getDataset() {
    return dataset;
  }
  
  protected String getInstanceSet() {
	return header;
  }
  
  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    super.setup(context);
    
    Configuration conf = context.getConfiguration();
    
    configure(!Builder.isOutput(conf), Builder.getPGgeneratorBuilder(conf), Builder.loadDataset(conf), Builder.getHeader(conf));
  }
  
  /**
   * Useful for testing
   */
  protected void configure(boolean noOutput, PGgenerator pg_algorithm, Dataset dataset, String header) {
    Preconditions.checkArgument(pg_algorithm != null, "PGgenerator not found in the Job parameters");
    this.noOutput = noOutput;
    this.pg_algorithm = pg_algorithm;
    this.dataset = dataset;
    this.header = header;
  }
}


