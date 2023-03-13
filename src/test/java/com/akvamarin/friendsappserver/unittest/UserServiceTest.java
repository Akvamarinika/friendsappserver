package com.akvamarin.friendsappserver.unittest;

import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Юнит тестирование UserService
 * @see UserService
 * */

@SpringBootTest
class UserServiceTest {

	private static final long USER_ID = 1L;
	private static final long NOT_FOUND_USER_ID = 100L;

	@Mock
	UserMapper userMapper;

	@Mock
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	/**
	 * Тестирование метода createNewUser()
	 * Сохранение записи в таблицу
	 * */
	@Test
	public void whenCreateNewUser_ThenUserObject() {
		final UserDTO dto = UserDTO.builder()
				.email("irinakn@mail.ru")
				.phone("+79501112323")
				.password("s123456")
				.nickname("Irina")
				.dateOfBirthday(LocalDate.of(2000,8,10))
				.urlAvatar("http://********")
				.build();

		assertInstanceOf(User.class, userService.createNewUser(dto));
	}


	@Sql({"classpath:city.sql", "classpath:users.sql" })
	@Test
	public void whenFindById_ThenReturnUser() {
		User user = User.builder()
				.id(USER_ID)
				.email("akvamarin@gmail.com")
				.phone("+79991210000")
				.password("$2a$10$ZmJxIt7HaqxXqwpvd7scte0UmLndHSn2DgZVS99Ug0xZRck2rJ6fO")
				.nickname("Akvamarin")
				.dateOfBirthday(LocalDate.of(1995,3,3))
				.urlAvatar("http://********")
				.createdAt(LocalDateTime.parse("2023-03-14T18:25:19.904272"))
				.updatedAt(LocalDateTime.parse("2023-03-14T18:25:19.904272"))
				.build();

		UserDTO expectedUserDTO = UserDTO.builder()
				.id(USER_ID)
				.email("akvamarin@gmail.com")
				.phone("+79991210000")
				.password("$2a$10$ZmJxIt7HaqxXqwpvd7scte0UmLndHSn2DgZVS99Ug0xZRck2rJ6fO")
				.nickname("Akvamarin")
				.dateOfBirthday(LocalDate.of(1995,3,3))
				.urlAvatar("http://********")
				.build();

		doReturn(Optional.of(user))
				.when(userRepository)
				.findById(USER_ID);

		//doReturn(expectedUserDTO).when(userMapper.toDTO(user));
		Mockito.when(userMapper.toDTO(user)).thenReturn(expectedUserDTO);

		Assertions.assertTrue(userRepository.findById(USER_ID).isPresent());	//если существует объект
		var actualResult = userService.findById(USER_ID);

		Assertions.assertEquals(expectedUserDTO.getId(), actualResult.getId());
		Assertions.assertEquals(expectedUserDTO.getEmail(), actualResult.getEmail());

		Mockito.verify(userRepository, Mockito.times(1))		//ожидаемое количество вызовов
				.findById(USER_ID);
	}

	@Test
	public void whenFindByIdAndNotFoundUser_ThenReturnException() throws Exception {
		final User user = null;

		doReturn(Optional.ofNullable(user))
				.when(userRepository)
				.findById(NOT_FOUND_USER_ID);

		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			userService.findById(NOT_FOUND_USER_ID);
		});

	}

}
