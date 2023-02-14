package swp.happyprogramming.adapter.config;

import javax.sql.DataSource;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAppConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    return modelMapper;
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }
}
