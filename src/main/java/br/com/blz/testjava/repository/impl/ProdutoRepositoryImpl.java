package br.com.blz.testjava.repository.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import br.com.blz.testjava.model.Produto;
import br.com.blz.testjava.model.exception.ExistingProductException;
import br.com.blz.testjava.repository.IProdutoRepository;

@Repository
public class ProdutoRepositoryImpl implements IProdutoRepository {

	Set<Produto> produtos = new HashSet<Produto>();

	@Override
	public Produto save(Produto produto) throws ExistingProductException {

		if (!produtos.add(produto)) {
			throw new ExistingProductException(produto.getSku());
		}

		return produto;
	}

	@Override
	public Optional<Produto> findBySku(Long sku) {
		return produtos.stream().filter(p -> p.getSku().equals(sku)).findAny();
	}

	@Override
	public void deleteProduto(Long sku) {

		Optional<Produto> produto = findBySku(sku);
		if (produto.isPresent()) {
			produtos.remove(produto.get());
		}
	}

	@Override
	public Produto editProduto(Produto produto) {
		
		Produto produtoNovo = produtos.stream().filter(prod -> prod.equals(produto)).map(prod -> produto).collect(Collectors.toList()).get(0);
		if(produtoNovo !=null) {
			this.deleteProduto(produtoNovo.getSku());
			try {
				this.save(produtoNovo);
			} catch (ExistingProductException e) {
				e.printStackTrace();
			}
			
		}
		
		return produtoNovo;
	}

}
