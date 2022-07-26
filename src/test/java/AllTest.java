import dao.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import po.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AllTest {
    private SqlSessionFactoryBuilder ssb;
    private InputStream is;
    private SqlSessionFactory factory;
    private UserMapper mapper;
    private SqlSession session;

    @Before
    public  void prepare(){
        ssb = new SqlSessionFactoryBuilder();
        try {
            is = Resources.getResourceAsStream("mybatis.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        factory = ssb.build(is);
        session = factory.openSession();
        mapper = session.getMapper(UserMapper.class);
    }
    @Test
    public void testFindUser(){
        List<User> users = mapper.findAllUsers();
        int i=0;
        for (User user : users) {
            System.out.println(user);
            i++;
        }
        System.out.println("一共" + i + "人");
    }
    @Test
    public void testAddUser(){
        mapper.addUser(new User(null,"田","123",500.0,null,"1",null));
        List<User> users = mapper.findAllUsers();
        int i=0;
        for (User user : users) {
            System.out.println(user);
            i++;
        }
        System.out.println("一共" + i + "人");
    }

    @Test
    public void testFindUserById(){
        User user = mapper.findUserById("5");
        System.out.println(user);
    }
    @Test
    public void testFindUserByName(){
        System.out.println(mapper.findUserByName("金11"));
    }
    @Test
    public void testUpdateUser(){
        User user = mapper.findUserById("5");
        user.setUserName("金1");
        mapper.updateUserById(user);
    }

    @After
    public void end() {
        session.commit();
        session.close();
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
