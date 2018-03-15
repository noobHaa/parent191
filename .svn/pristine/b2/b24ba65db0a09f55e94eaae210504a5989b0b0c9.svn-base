package cn.itcast.core.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.bean.user.BuyerQuery;
import cn.itcast.core.dao.user.BuyerDao;

/**
 * 用户管理
 * @author lx
 *
 */
@Service("buyerService")
public class BuyerServiceImpl implements BuyerService{

	@Autowired
	private BuyerDao buyerDao;
	
	//通过用户名查询用户对象
	public Buyer selectBuyerByUsername(String username){
		BuyerQuery buyerQuery = new BuyerQuery();
		buyerQuery.createCriteria().andUsernameEqualTo(username);
		
		List<Buyer> buyers = buyerDao.selectByExample(buyerQuery);
		if(null != buyers && buyers.size() > 0){
			return buyers.get(0);
		}
		return null;
	}
}
