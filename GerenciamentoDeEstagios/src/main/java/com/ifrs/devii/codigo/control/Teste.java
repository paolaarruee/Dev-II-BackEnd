package com.ifrs.devii.codigo.control;

import com.ifrs.devii.codigo.file.SalvarDocumento;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class Teste {

    SalvarDocumento salvarDocumento = new SalvarDocumento();
    @PostMapping("/salvar")
    @ResponseBody
    public String salvarDocumentos(@RequestBody MultipartFile documento){
        //simular id de chamado
        int chamado = (int) (Math.random() * 10000);
        return salvarDocumento.salvar(documento,chamado);
    }

    //faz download de um unico arquivo, precisa do nome do arquivo e o chamadoId (pasta), que no teste está 320...
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadArquivo(@RequestParam(name = "nomeArquivo") String nomeArquivo) {
        Path diretorioDestino = Paths.get("./documentos/chamados/320/" + nomeArquivo);
        try {
            Resource resource = new UrlResource(diretorioDestino.toUri());
            if (!resource.exists()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            String contentType = URLConnection.guessContentTypeFromName(resource.getFilename());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listar")
    @ResponseBody
    public String listarDocumentos(@RequestParam String chamadoId){
        List<String> lista = salvarDocumento.listarDocumentos(chamadoId);
        String nomes = "";
        for (String nome : lista){
            nomes += "\n " + nome.toString();
        }
        return nomes;
    }


}
