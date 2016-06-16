
package com.igomall.service;

import java.util.List;

import com.igomall.entity.Product;

public interface ParseCSVService {

	List<Product> parseTaobao(String file);

	List<Product> parseAliBaBa(String file);


}