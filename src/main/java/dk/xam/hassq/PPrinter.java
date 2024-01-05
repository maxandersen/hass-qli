package dk.xam.hassq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.inject.Singleton;

@Singleton
public class PPrinter {

    DefaultPrettyPrinter prettyPrinter;

    ObjectMapper mapper;
    
    public PPrinter(ObjectMapper mapper) {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        //uncomment to be strict  
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        this.mapper = mapper;

        prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
    }

    public String string(Object data) {
        try {
            if(data==null) {
                return "null";
            } 
            if(data instanceof String) {
                return (String)data;
            }
            return mapper.writer(prettyPrinter).writeValueAsString(data);
        } catch (JsonProcessingException e) {
            //if error serializing, just return toString()
            return data.toString();
        }
    }
}