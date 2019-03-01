package cn.pay.quickpay.xmlbean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement( name = "agent" )
public class AgentXmlBean {

	private int id;//
	private String agent_name;//机构名称
	private String agent_fullname;//全称
	private String agent_no;//机构号
	private String db_path;//数据库路径
	private String platform_password;//平台密码
	private String secret_key;//密钥
	private String linkman;//联系人
	private String linkphone;//联系电话
	private String agency_id;//代理商id
	private String email;//邮箱
	private boolean is_open;//是否启用
	private String db_driver;//
	private String db_url;//
	private String db_username;//
	private String db_password;//
	private int is_operator;//运营商标识 0否1是 
	private int is_auth;//是否鉴权（ 0否1是 ）
	private String ip_white_list;//ip白名单
	private String certificate;//营业执照号或社会统一代码
	private String address;//公司地址
	private List<VirtualChannelXmlBean> channelList;//无积分通道
	private List<VirtualChannelXmlBean> integralChannelList;//有积分通道
	private List<VirtualChannelXmlBean> repaymentList;//代还通道
	private List<VirtualChannelXmlBean> balaceList;//结算通道
	private List<VirtualChannelXmlBean> upgradeList;//升级通道
	private List<VirtualChannelXmlBean> delimitList;//划扣通道
	private List<AuthChannelProductOfAgentXmlBean> authList;//鉴权产品列表
	private List<LoanVirtualChannelXmlBean> loanList;//贷款产品列表
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAgent_name() {
		return agent_name;
	}
	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}
	public String getAgent_fullname() {
		return agent_fullname;
	}
	public void setAgent_fullname(String agent_fullname) {
		this.agent_fullname = agent_fullname;
	}
	public String getAgent_no() {
		return agent_no;
	}
	public void setAgent_no(String agent_no) {
		this.agent_no = agent_no;
	}
	public String getDb_path() {
		return db_path;
	}
	public void setDb_path(String db_path) {
		this.db_path = db_path;
	}
	public String getPlatform_password() {
		return platform_password;
	}
	public void setPlatform_password(String platform_password) {
		this.platform_password = platform_password;
	}
	public String getSecret_key() {
		return secret_key;
	}
	public void setSecret_key(String secret_key) {
		this.secret_key = secret_key;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getLinkphone() {
		return linkphone;
	}
	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}
	public String getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(String agency_id) {
		this.agency_id = agency_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isIs_open() {
		return is_open;
	}
	public void setIs_open(boolean is_open) {
		this.is_open = is_open;
	}
	public String getDb_driver() {
		return db_driver;
	}
	public void setDb_driver(String db_driver) {
		this.db_driver = db_driver;
	}
	public String getDb_url() {
		return db_url;
	}
	public void setDb_url(String db_url) {
		this.db_url = db_url;
	}
	public String getDb_username() {
		return db_username;
	}
	public void setDb_username(String db_username) {
		this.db_username = db_username;
	}
	public String getDb_password() {
		return db_password;
	}
	public void setDb_password(String db_password) {
		this.db_password = db_password;
	}
	public int getIs_operator() {
		return is_operator;
	}
	public void setIs_operator(int is_operator) {
		this.is_operator = is_operator;
	}
	@XmlElementWrapper(name = "channel_list") 
	@XmlElement(name = "item")
	public List<VirtualChannelXmlBean> getChannelList() {
		return channelList;
	}
	public void setChannelList(List<VirtualChannelXmlBean> channelList) {
		this.channelList = channelList;
	}
	@XmlElementWrapper(name = "integral_channel_list") 
	@XmlElement(name = "item")
	public List<VirtualChannelXmlBean> getIntegralChannelList() {
		return integralChannelList;
	}
	public void setIntegralChannelList(
			List<VirtualChannelXmlBean> integralChannelList) {
		this.integralChannelList = integralChannelList;
	}
	@XmlElementWrapper(name = "repayment_list") 
	@XmlElement(name = "item")
	public List<VirtualChannelXmlBean> getRepaymentList() {
		return repaymentList;
	}
	public void setRepaymentList(List<VirtualChannelXmlBean> repaymentList) {
		this.repaymentList = repaymentList;
	}
	@XmlElementWrapper(name = "balace_list") 
	@XmlElement(name = "item")
	public List<VirtualChannelXmlBean> getBalaceList() {
		return balaceList;
	}
	public void setBalaceList(List<VirtualChannelXmlBean> balaceList) {
		this.balaceList = balaceList;
	}
	@XmlElementWrapper(name = "upgrade_list") 
	@XmlElement(name = "item")
	public List<VirtualChannelXmlBean> getUpgradeList() {
		return upgradeList;
	}
	public void setUpgradeList(List<VirtualChannelXmlBean> upgradeList) {
		this.upgradeList = upgradeList;
	}
	@XmlElementWrapper(name = "delimit_list") 
	@XmlElement(name = "item")
	public List<VirtualChannelXmlBean> getDelimitList() {
		return delimitList;
	}
	public void setDelimitList(List<VirtualChannelXmlBean> delimitList) {
		this.delimitList = delimitList;
	}
	@XmlElementWrapper(name = "auth_list") 
	@XmlElement(name = "item")
	public List<AuthChannelProductOfAgentXmlBean> getAuthList() {
		return authList;
	}
	public void setAuthList(List<AuthChannelProductOfAgentXmlBean> authList) {
		this.authList = authList;
	}
	public int getIs_auth() {
		return is_auth;
	}
	public void setIs_auth(int is_auth) {
		this.is_auth = is_auth;
	}
	public String getIp_white_list() {
		return ip_white_list;
	}
	public void setIp_white_list(String ip_white_list) {
		this.ip_white_list = ip_white_list;
	}
	public String getCertificate() {
		return certificate;
	}
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@XmlElementWrapper(name = "loan_list") 
	@XmlElement(name = "item")
	public List<LoanVirtualChannelXmlBean> getLoanList() {
		return loanList;
	}
	public void setLoanList(List<LoanVirtualChannelXmlBean> loanList) {
		this.loanList = loanList;
	}
	@Override
	public String toString() {
		return "AgentXmlBean [id=" + id + ", agent_name=" + agent_name
				+ ", agent_fullname=" + agent_fullname + ", agent_no="
				+ agent_no + ", db_path=" + db_path + ", platform_password="
				+ platform_password + ", secret_key=" + secret_key
				+ ", linkman=" + linkman + ", linkphone=" + linkphone
				+ ", agency_id=" + agency_id + ", email=" + email
				+ ", is_open=" + is_open + ", db_driver=" + db_driver
				+ ", db_url=" + db_url + ", db_username=" + db_username
				+ ", db_password=" + db_password + ", is_operator="
				+ is_operator + ", is_auth=" + is_auth + ", ip_white_list="
				+ ip_white_list + ", certificate=" + certificate + ", address="
				+ address + ", channelList=" + channelList
				+ ", integralChannelList=" + integralChannelList
				+ ", repaymentList=" + repaymentList + ", balaceList="
				+ balaceList + ", upgradeList=" + upgradeList
				+ ", delimitList=" + delimitList + ", authList=" + authList
				+ ", loanList=" + loanList + "]";
	}
	
	
	
}
