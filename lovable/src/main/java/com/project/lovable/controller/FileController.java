package com.project.lovable.controller;

import com.project.lovable.dto.project.FileContentResponse;
import com.project.lovable.dto.project.FileNode;
import com.project.lovable.dto.project.FileTreeResponse;
import com.project.lovable.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/REDACTED/{projectId}/files")
@RequiredArgsConstructor
public class FileController {

    private final ProjectFileService projectFileService;

    @GetMapping
    public ResponseEntity<FileTreeResponse> getFileTree(@PathVariable Long projectId){

        Long userId = 1L;
        return ResponseEntity.ok(projectFileService.getFileTree(projectId));

    }

    @GetMapping("/content")
    public ResponseEntity<FileContentResponse> getFile(@PathVariable Long projectId, @RequestParam String path){

        Long userId = 1L;

        return ResponseEntity.ok(projectFileService.getFileContent(projectId, path));

    }


}
