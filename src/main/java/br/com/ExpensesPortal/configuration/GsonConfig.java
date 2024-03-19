package br.com.ExpensesPortal.configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, adaptLocalDateTime())
                .create();
    }

    private TypeAdapter<LocalDateTime> adaptLocalDateTime() {
        return new TypeAdapter<LocalDateTime>() {
            private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            @Override
            public void write(JsonWriter out, LocalDateTime value) throws IOException {
                out.value(value != null ? formatter.format(value) : null);
            }

            @Override
            public LocalDateTime read(JsonReader in) throws IOException {
                return LocalDateTime.parse(in.nextString(), formatter);
            }
        };
    }
}