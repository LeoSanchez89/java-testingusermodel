package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplicationTesting;
import com.lambdaschool.usermodel.exceptions.ResourceNotFoundException;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.repository.RoleRepository;
import com.lambdaschool.usermodel.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplicationTesting.class,
    properties = {"command.line.runner.enabled=false"})
public class UserServiceImplUnitTestNoDB {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

//    Mock DB
    List<User> userList = new ArrayList<>();
    @Before
    public void setUp() throws Exception {

        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1.setRoleid(1);
        r2.setRoleid(2);
        r3.setRoleid(3);

        // admin, data, user
        User u1 = new User("Test admin",
                "password",
                "admin@lambdaschool.local");
        u1.setUserid(10);
        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r2));
        u1.getRoles()
                .add(new UserRoles(u1,
                        r3));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));
        u1.getUseremails().get(0).setUseremailid(11);
        u1.getUseremails().get(1).setUseremailid(12);
        userList.add(u1);

        // data, user
        User u2 = new User("Test cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local");
        u2.setUserid(20);
        u2.getRoles()
                .add(new UserRoles(u2,
                        r2));
        u2.getRoles()
                .add(new UserRoles(u2,
                        r3));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        u2.getUseremails().get(0).setUseremailid(21);
        u2.getUseremails().get(1).setUseremailid(22);
        u2.getUseremails().get(2).setUseremailid(23);
        userList.add(u2);

        // user
        User u3 = new User("Test barnbarn",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local");
        u2.setUserid(30);
        u3.getRoles()
                .add(new UserRoles(u3,
                        r2));
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        u3.getUseremails().get(0).setUseremailid(31);
        userList.add(u3);

        User u4 = new User("Test puttat",
                "password",
                "puttat@school.lambda");
        u4.setUserid(40);
        u4.getRoles()
                .add(new UserRoles(u4,
                        r2));
        userList.add(u4);

        User u5 = new User("Test misskitty",
                "password",
                "misskitty@school.lambda");
        u5.setUserid(50);
        u5.getRoles()
                .add(new UserRoles(u5,
                        r2));
        userList.add(u5);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findUserById() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(userList.get(0)));
        assertEquals("test admin", userService.findUserById(1).getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findUserByIdNotFound() {
        Mockito.when(userRepository.findById(100L))
                .thenReturn(Optional.empty());
        assertEquals("test admin", userService.findUserById(100).getUsername());
    }

    @Test
    public void findByNameContaining() {
        Mockito.when(userRepository.findByUsernameContainingIgnoreCase("bar"))
                .thenReturn(userList);
        assertEquals(5, userService.findByNameContaining("bar").size());
    }

    @Test
    public void findAll() {
        Mockito.when(userRepository.findAll())
                .thenReturn(userList);
        assertEquals(5, userService.findAll().size());
    }

    @Test
    public void delete() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(userList.get(0)));

        Mockito.doNothing()
                .when(userRepository)
                .deleteById(1L);

        userService.delete(1);
        assertEquals(5, userList.size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteUserNotFound() {
        Mockito.when(userRepository.findById(420L))
                .thenReturn(Optional.empty());

        Mockito.doNothing()
                .when(userRepository)
                .deleteById(420L);

        userService.delete(420);
        assertEquals(5, userList.size());
    }

    @Test
    public void findByName() {
        Mockito.when(userRepository.findByUsername("test admin"))
                .thenReturn(userList.get(0));
        assertEquals(userList.get(0), userService.findByName("test admin"));

    }

    @Test
    public void savePost() {
        User newUser = new User("Scooty McBones", "69help69", "rattle@my.bones");
        Role role1 = new Role("GOONIE");
        role1.setRoleid(69);
        newUser.getUseremails().add(new Useremail(newUser, "shiver@me.timbers"));
        newUser.getRoles().add(new UserRoles(newUser, role1));

        Mockito.when(roleRepository.findById(69L))
                .thenReturn(Optional.of(role1));

        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(newUser);

        User addUser = userService.save(newUser);
        assertNotNull(addUser);
        assertEquals(newUser.getUsername(), addUser.getUsername());
        assertEquals(newUser.getUserid(), addUser.getUserid());
        assertEquals(newUser.getRoles(), addUser.getRoles());
        assertEquals(newUser.getUseremails(), addUser.getUseremails());
    }

    @Test
    public void update() {
    }

    @Test
    public void deleteAll() {
    }
}
