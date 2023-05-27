import Enum from '@/common/Enum.js';

class LoginMethodEnum extends Enum {
  /**
   * 枚举对象
   */
  static MAIL = [0, '邮箱登陆'];
  static WEIXIN = [1, '微信登陆'];

  /**
   * 状态
   */
  static code;

  /**
   * 描述
   */
  static desc;
}

const obj = new LoginMethodEnum();

export default obj;
