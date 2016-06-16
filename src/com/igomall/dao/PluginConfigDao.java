/*
 * 
 * 
 * 
 */
package com.igomall.dao;

import com.igomall.entity.PluginConfig;

public interface PluginConfigDao extends BaseDao<PluginConfig, Long> {

	boolean pluginIdExists(String pluginId);

	PluginConfig findByPluginId(String pluginId);

}