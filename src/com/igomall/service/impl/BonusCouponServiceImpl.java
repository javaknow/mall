
package com.igomall.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igomall.Filter;
import com.igomall.Order;
import com.igomall.Page;
import com.igomall.Pageable;
import com.igomall.dao.BonusCouponDao;
import com.igomall.dao.MemberDao;
import com.igomall.dao.MoneyFlowingWaterDao;
import com.igomall.dao.ShopDao;
import com.igomall.entity.BonusCoupon;
import com.igomall.entity.Member;
import com.igomall.entity.MoneyFlowingWater;
import com.igomall.entity.OrderItem;
import com.igomall.entity.Product;
import com.igomall.entity.Promotion;
import com.igomall.entity.Shop;
import com.igomall.entity.BonusCoupon.Type;
import com.igomall.service.BonusCouponService;
import com.igomall.util.ChangeDate;

@Service("bonusCouponServiceImpl")
public class BonusCouponServiceImpl extends BaseServiceImpl<BonusCoupon, Long> implements BonusCouponService {

	@Resource(name = "bonusCouponDaoImpl")
	public BonusCouponDao bonusCouponDao;
	@Resource(name = "memberDaoImpl")
	public MemberDao memberDao;
	@Resource(name = "shopDaoImpl")
	public ShopDao shopDao;
	
	@Resource(name = "moneyFlowingWaterDaoImpl")
	public MoneyFlowingWaterDao moneyFlowingWaterDao;

	@Resource(name = "bonusCouponDaoImpl")
	public void setBaseDao(BonusCouponDao bonusCouponDao) {
		super.setBaseDao(bonusCouponDao);
	}

	@Transactional(readOnly = true)
	public List<BonusCoupon> findList(Type type) {
		return bonusCouponDao.findList(type);
	}

	@Transactional(readOnly = true)
	public List<BonusCoupon> findList(Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
		return bonusCouponDao.findList(null, count, filters, orders);
	}

	@Override
	@Transactional
	public void save(BonusCoupon bonusCoupon) {
		super.save(bonusCoupon);
	}

	@Override
	@Transactional
	public BonusCoupon update(BonusCoupon bonusCoupon) {
		return super.update(bonusCoupon);
	}

	@Override
	@Transactional
	public BonusCoupon update(BonusCoupon bonusCoupon, String... ignoreProperties) {
		return super.update(bonusCoupon, ignoreProperties);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	public void delete(BonusCoupon bonusCoupon) {
		super.delete(bonusCoupon);
	}

	@Override
	public void create(com.igomall.entity.Order order, Type type,HttpServletRequest request) {
		
		Member member = order.getMember();
		BigDecimal percent = new BigDecimal(100);
		
		List<OrderItem> orderItems = order.getOrderItems();
		Map<Shop,List<OrderItem>> map = splitByShop(orderItems);
		List<OrderItem> list = new ArrayList<OrderItem>();
		BigDecimal shopBalance = new BigDecimal(0);
		
		Set<Shop> shops = map.keySet();
		for (Shop shop : shops) {
			list = map.get(shop);
			shopBalance  = calShopBalance(list);
			shop.setBalance(shop.getBalance().add(shopBalance.multiply(shop.getRate1())));
			shopDao.persist(shop);
			
			Date endDate = ChangeDate.getNextDay(shop.getDays()).toDate();
			BigDecimal balance = shopBalance.multiply(shop.getBuyerRate());
			BonusCoupon bonusCoupon = new BonusCoupon();
			if(balance.compareTo(BigDecimal.ZERO)>0){
				
				bonusCoupon.setMemo("在店铺 "+ shop.getName()+" 确定订单["+shopBalance+"]，获取 "+(shop.getBuyerRate().setScale(2)).multiply(percent)+"%的奖金券！");
				bonusCoupon.setShop(shop);
				bonusCoupon.setRate(shop.getRate3());
				bonusCoupon.setEndDate(endDate);
				bonusCoupon.setMember(member);
				bonusCoupon.setBalance(balance);
				bonusCoupon.setBalance1(BigDecimal.ZERO);
				bonusCoupon.setBalance2(shopBalance.multiply(bonusCoupon.getRate()));
				bonusCoupon.setType(type);
				bonusCoupon.setOrders(order);
				bonusCoupon.setIsAlloted(false);
				bonusCouponDao.merge(bonusCoupon);
				
				create(member, balance, MoneyFlowingWater.Type.balance1, "在店铺 "+ shop.getName()+" 确定订单确认订单["+shopBalance+"]，分配奖金券", request,false);
				
			}
			
			Member parent = member.getParent();
			if(parent!=null){
				bonusCoupon = new BonusCoupon();
				
				bonusCoupon.setMemo("会员："+member.getUsername()+"在店铺 "+ shop.getName()+" 确定订单["+shopBalance+"]，获取直推 "+(shop.getBuyerParentRate().setScale(2)).multiply(percent)+"%的奖金券！");
				balance = shopBalance.multiply(shop.getBuyerParentRate());
				if(balance.compareTo(BigDecimal.ZERO)>0){
					bonusCoupon.setShop(shop);
					bonusCoupon.setRate(shop.getRate3());
					bonusCoupon.setEndDate(endDate);
					bonusCoupon.setMember(parent);
					bonusCoupon.setBalance(balance);
					bonusCoupon.setBalance1(BigDecimal.ZERO);
					bonusCoupon.setBalance2(new BigDecimal(1000000));
					bonusCoupon.setType(type);
					bonusCoupon.setOrders(null);
					bonusCoupon.setIsAlloted(false);
					bonusCouponDao.merge(bonusCoupon);
					
					create(parent, balance, MoneyFlowingWater.Type.balance1, "会员："+member.getUsername()+"在店铺 "+ shop.getName()+" 确定订单["+shopBalance+"]，分配奖金券", request,false);
					
				}
				
				
				parent=parent.getParent();
				if(parent!=null){
					bonusCoupon = new BonusCoupon();
					
					bonusCoupon.setMemo("会员："+member.getUsername()+"在店铺 "+ shop.getName()+" 确定订单["+shopBalance+"]，获取间推 "+(shop.getBuyerParentParentRate().setScale(2)).multiply(percent)+"%的奖金券！");
					balance = shopBalance.multiply(shop.getBuyerParentParentRate());
					if(balance.compareTo(BigDecimal.ZERO)>0){
						bonusCoupon.setShop(shop);
						bonusCoupon.setRate(shop.getRate3());
						bonusCoupon.setEndDate(endDate);
						bonusCoupon.setMember(parent);
						bonusCoupon.setBalance(balance);
						bonusCoupon.setBalance1(BigDecimal.ZERO);
						bonusCoupon.setBalance2(new BigDecimal(1000000));
						bonusCoupon.setType(type);
						bonusCoupon.setOrders(null);
						bonusCoupon.setIsAlloted(false);
						bonusCouponDao.merge(bonusCoupon);
						
						create(parent, balance, MoneyFlowingWater.Type.balance, "会员："+member.getUsername()+"在店铺 "+ shop.getName()+" 确定订单["+shopBalance+"]，分配奖金券", request,false);
					}
					
				}
			}
		}
		
	}

	public BigDecimal calShopBalance(List<OrderItem> list) {
		BigDecimal balance = new BigDecimal(0);
		for (OrderItem orderItem : list) {
			if(orderItem.getPromotion()!=null){//产品参加了促销或活动
				Promotion promotion = orderItem.getPromotion();
				Member parent = orderItem.getParent();
				Member member = orderItem.getOrder().getMember();
				Shop shop = orderItem.getProduct().getShop();
				if(promotion!=null&&parent!=null){
System.out.println(promotion.getId());
					BigDecimal balance1 = orderItem.getAmountPaid().multiply(new BigDecimal(promotion.getRate())).divide(new BigDecimal(100));
					//parent.setBalance(parent.getBalance().add(balance1));
					create(parent, balance1.multiply(new BigDecimal(orderItem.getQuantity())), MoneyFlowingWater.Type.balance, "会员："+member.getUsername()+"在店铺 "+ shop.getName()+" 确定订单["+balance1.multiply(new BigDecimal(orderItem.getQuantity()))+"]，分配"+promotion.getRate()+"%提成", null,false);
				}
			}else{//产品没有参加促销活动
				balance = balance.add(orderItem.getAmountPaid());
			}
		}
		return balance;
	}

	/**
	 * 把订单子表按照店铺进行分组
	 * @param orderItems
	 * @return
	 */
	public Map<Shop, List<OrderItem>> splitByShop(List<OrderItem> orderItems) {
		Map<Shop, List<OrderItem>> map = new HashMap<Shop,List<OrderItem>>();
		List<OrderItem> list = new ArrayList<OrderItem>();
		
		for (OrderItem orderItem : orderItems) {
			Product product = orderItem.getProduct();
			if(product!=null){
				Shop shop = product.getShop();
				if(map.containsKey(shop)){
					map.get(shop).add(orderItem);
				}else{
					list.add(orderItem);
					map.put(shop, list);
				}
			}
		}		
		
		return map;
	}

	@Override
	public Page<BonusCoupon> findPage(Member member, Pageable pageable,Type type) {
		return bonusCouponDao.findPage(member, pageable,type);
	}

	@Override
	public List<BonusCoupon> findListByShop(Shop shop,Type type,Boolean isOut,Boolean hasEnd, Boolean isAlloted) {
		return bonusCouponDao.findListByShop(shop,type,isOut,hasEnd,isAlloted);
	}

	@Override
	public BigDecimal countByShop(Shop shop, Type type,Boolean isOut,Boolean hasEnd, Boolean isAlloted) {
		return bonusCouponDao.countByShop(shop,type,isOut,hasEnd,isAlloted);
	}

	@Override
	public void create(BonusCoupon parent, Type type, BigDecimal balance) {
		Member member = parent.getMember();
			Shop shop = parent.getShop();
			BonusCoupon bonusCoupon = new BonusCoupon();
			
			bonusCoupon.setParent(parent);
			bonusCoupon.setMemo("商家发送奖金券");
			bonusCoupon.setShop(shop);
			bonusCoupon.setRate(BigDecimal.ZERO);
			bonusCoupon.setEndDate(null);
			bonusCoupon.setMember(member);
			bonusCoupon.setBalance(balance);
			bonusCoupon.setType(type);
			bonusCouponDao.merge(bonusCoupon);
	}

	@Override
	public Page<BonusCoupon> findPage(Pageable pageable, Shop shop, Type type) {
		return bonusCouponDao.findPage(pageable, shop,type);
	}

	@Override
	public Page<BonusCoupon> findPage(Pageable pageable, BonusCoupon bonusCoupon, Type type) {
		return bonusCouponDao.findPage(pageable, bonusCoupon,type);
	}

	@Override
	public Page<BonusCoupon> findPage(Member member, Pageable pageable, Type type, BonusCoupon bonusCoupon) {
		return bonusCouponDao.findPage(member,pageable,type,bonusCoupon);
	}

	@Override
	public void deveryBonusCoupns() {
		/**
		 * 1.查询可用的店铺
		 * 2.循环店铺查询没有出局和没有过期的奖金券
		 * 3.计算每个奖金券的分配金额
		 * 4.给会员发放奖金
		 */
		List<Shop> shops = shopDao.findList(true);
		for (Shop shop : shops) {
			List<BonusCoupon> bonusCoupons = bonusCouponDao.findListByShop(shop,Type.member,false,false,false);
			BigDecimal bonusCouponsBalance = bonusCouponDao.countByShop(shop,Type.member,false,false,false);
			if(bonusCouponsBalance.compareTo(BigDecimal.ZERO)>0&&shop.getBalance().compareTo(BigDecimal.ZERO)>0){
				BigDecimal deliveryBalance= shop.getBalance().multiply(shop.getRate2());
				BigDecimal everyBalance = deliveryBalance.divide(bonusCouponsBalance,10,RoundingMode.DOWN);
				shop.setBalance(shop.getBalance().subtract(deliveryBalance));
				shopDao.merge(shop);
				
				for (BonusCoupon bonusCoupon : bonusCoupons) {
					Member member = bonusCoupon.getMember();
					BigDecimal tempEveryBalance = everyBalance.multiply(bonusCoupon.getBalance());
					
					if((bonusCoupon.getBalance1().add(tempEveryBalance)).compareTo(bonusCoupon.getBalance2())>0){
						tempEveryBalance = bonusCoupon.getBalance2().subtract(bonusCoupon.getBalance1());
						
					}
					bonusCoupon.setBalance1(bonusCoupon.getBalance1().add(tempEveryBalance));
					bonusCoupon.setIsAlloted(true);
					if(bonusCoupon.getOrders()==null){//不是通过订单获取的奖金券，那么就设置成已过期和已出局
						bonusCoupon.setBalance2(bonusCoupon.getBalance1());
						bonusCoupon.setEndDate(new Date());
					}
					
					bonusCouponDao.merge(bonusCoupon);
					
					create(member, tempEveryBalance, MoneyFlowingWater.Type.balance, "商家  "+ shop.getName()+" 分配奖金券", null,true);
					
					create(bonusCoupon,Type.shop,tempEveryBalance);
				}
			}
			

		}
	}
	
	/**
	 * 创建资金流水记录
	 * @param member
	 * @param balance
	 * @param type
	 * @param content
	 * @param request
	 */
	public void create(Member member, BigDecimal balance, MoneyFlowingWater.Type type,String content,HttpServletRequest request,Boolean flag) {
		MoneyFlowingWater moneyFlowingWater = new MoneyFlowingWater();
		if(type==MoneyFlowingWater.Type.balance1){//奖金券的变动
			moneyFlowingWater.setBeforeBalance(member.getBalance1());
			member.setBalance1(member.getBalance1().add(balance));
			moneyFlowingWater.setAfterBalance(member.getBalance1());
		}else if(type==MoneyFlowingWater.Type.recharge){
			moneyFlowingWater.setBeforeBalance(member.getBalance());
			member.setBalance(member.getBalance().add(balance));
			moneyFlowingWater.setAfterBalance(member.getBalance());
			
		}else if(type==MoneyFlowingWater.Type.withdraw){
			moneyFlowingWater.setBeforeBalance(member.getBalance());
			member.setBalance(member.getBalance().add(balance));
			moneyFlowingWater.setAfterBalance(member.getBalance());
			
		}else if(type==MoneyFlowingWater.Type.balance){//资金账户进行变动
			moneyFlowingWater.setBeforeBalance(member.getBalance());
			member.setBalance(member.getBalance().add(balance));
			moneyFlowingWater.setAfterBalance(member.getBalance());
		}
		if(flag){
			member.setBalance1(BigDecimal.ZERO);
		}
		memberDao.merge(member);
		
		if (request!=null) {
			moneyFlowingWater.setIp(request.getRemoteAddr());
		}
		moneyFlowingWater.setMember(member);
		moneyFlowingWater.setBalance(balance);
		moneyFlowingWater.setContent(content);
		moneyFlowingWater.setType(type);
		
		moneyFlowingWaterDao.persist(moneyFlowingWater);
		
	}

	@Override
	public BigDecimal count(Member member, Boolean isOut, Boolean hasEnd,Shop shop, Type type,Boolean isAlloted) {
		return bonusCouponDao.count(member,isOut,hasEnd,shop,type,isAlloted);
	}
	
	@Override
	public List<BonusCoupon> findList(Member member, Boolean isOut,Boolean hasEnd, Shop shop, Type type, Boolean isAlloted){
		return bonusCouponDao.findList(member, isOut,hasEnd, shop, type, isAlloted);
	}

}