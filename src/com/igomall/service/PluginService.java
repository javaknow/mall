
package com.igomall.service;

import java.util.List;

import com.igomall.plugin.PaymentPlugin;
import com.igomall.plugin.StoragePlugin;

public interface PluginService {

	List<PaymentPlugin> getPaymentPlugins();

	List<StoragePlugin> getStoragePlugins();

	List<PaymentPlugin> getPaymentPlugins(boolean isEnabled);

	List<StoragePlugin> getStoragePlugins(boolean isEnabled);

	PaymentPlugin getPaymentPlugin(String id);

	StoragePlugin getStoragePlugin(String id);

}