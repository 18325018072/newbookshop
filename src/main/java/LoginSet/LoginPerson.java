package LoginSet;

import dao.SqlUtils;
import dao.UserDao;
import lombok.Data;
import lombok.NoArgsConstructor;
import po.User;

import javax.swing.*;

//登录者对象,使用了单例模式
@Data@NoArgsConstructor
public class LoginPerson {
    private static User p;

    private LoginPerson(String username, String password) {
        UserDao userDao = new UserDao();
        User user = userDao.findUserByName(username);
        if (user.getPassword().equals(password)) {
            if ("0".equals(user.getLoginState())) {
                //设置登录状态
                user.setLoginState("1");
                userDao.updateUser(user);
                p=user;
            } else {
                JOptionPane.showMessageDialog(null, "登陆失败，该账号已登录", "警告", JOptionPane.WARNING_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(null, "用户名或密码错误", "警告", JOptionPane.WARNING_MESSAGE);
        }
        userDao.close();
    }

    //登录后获取对象
    public static synchronized User getInstance() {
        if (p == null) {
            JOptionPane.showMessageDialog(null, "系统异常，未登录", "消息对话框", JOptionPane.WARNING_MESSAGE);
            return null;
        } else {
            return p;
        }
    }

    //登录时获取对象
    public static synchronized User getInstance(String username, String password) {

        new LoginPerson(username, password);
        return p;
    }
}
