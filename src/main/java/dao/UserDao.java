package dao;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import po.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class UserDao {
    private static SqlSessionFactory factory;
    private static UserMapper mapper;
    private static SqlSession session;

    //静态代码块
    static {
        try {
            InputStream is = Resources.getResourceAsStream("mybatis.xml");
            factory = new SqlSessionFactoryBuilder().build(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //构造代码块，每次构造方法前执行
    {
        session = factory.openSession();
        mapper = session.getMapper(UserMapper.class);
    }

    public List<User> findUsers(){
        return mapper.findAllUsers();
    }

    public void addUser(User u){
        mapper.addUser(u);
        session.commit();
    }

    public User findUserById(String id){
        return mapper.findUserById(id);
    }

    public void deleteUserById(String id){
        mapper.deleteUserById(id);
        session.commit();
    }

    public void updateUser(User user){
        mapper.updateUserById(user);
        session.commit();
    }

    public User findUserByName(String name){
        return mapper.findUserByName(name);
    }

    public void close() {
        session.close();
    }
}
