package br.senac.tads.dsw.exemplo2.controller;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.senac.tads.dsw.exemplo2.model.Produto;
import br.senac.tads.dsw.exemplo2.repository.ProdutoRepository;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    
    private ProdutoRepository repository;

    public ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @PostMapping()
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        
        Produto produtoSalvo = repository.save(produto);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(produtoSalvo.getId())
            .toUri();

        return ResponseEntity.created(location).body(produtoSalvo);
    }

    // Retorna a lista completa de produtos armazenados no banco de dados.
@GetMapping
public List<Produto> listarTodos() {
    // O método findAll() do repositório gera automaticamente um SELECT * FROM produto.
    return repository.findAll();
}

// A notação {id} representa um parâmetro de caminho (PathVariable).
// A URL esperada é /api/produtos/1
@GetMapping("/{id}")
public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
    // O método findById retorna um Optional.
    Optional<Produto> produtoBuscado = repository.findById(id);

    if (produtoBuscado.isPresent()) {
        return ResponseEntity.ok(produtoBuscado.get());
    } else {
        return ResponseEntity.notFound().build();
    }
}
@PutMapping("/{id}")
public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {

    Optional<Produto> produtoBuscado = repository.findById(id);

    if (produtoBuscado.isPresent()) {
        Produto produtoExistente = produtoBuscado.get();

        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setPreco(produtoAtualizado.getPreco());

        Produto produtoSalvo = repository.save(produtoExistente);

        return ResponseEntity.ok(produtoSalvo);
    } else {
        return ResponseEntity.notFound().build();
    }
}
@DeleteMapping("/{id}")
public ResponseEntity<Void> apagarProduto(@PathVariable Long id) {
    Optional<Produto> produtoBuscado = repository.findById(id);

    if (produtoBuscado.isPresent()) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    } else {
        return ResponseEntity.notFound().build();
    }
}
}
