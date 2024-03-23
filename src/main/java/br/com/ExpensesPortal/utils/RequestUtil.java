package br.com.ExpensesPortal.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.ExpensesPortal.DTOs.requests.HttpDTO;

@Component
public class RequestUtil {

    public HttpDTO request(String urlString, HttpMethod method, String data, String token) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method.toString());

        // Adiciona o cabeçalho Authorization se um token foi fornecido
        if (token != null && !token.isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer "+token);
        }

        if (data != null) {
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes("UTF-8"));
            }
        }

        HttpStatus httpStatus = HttpStatus.valueOf(conn.getResponseCode());
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            // Aqui, você pode querer ler o InputStream de erro da conexão também
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
        }

        return new HttpDTO(token, httpStatus, response.toString());
    }

}
