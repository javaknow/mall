
package com.igomall.service;

import com.igomall.entity.ShiShiCai;

public interface ShiShiCaiService extends BaseService<ShiShiCai, Long> {

	ShiShiCai find(Long id, String cacheRegion);

}