package com.akvamarin.friendsappserver.unittest;

import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import com.akvamarin.friendsappserver.domain.responseerror.ValidationErrorResponse;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.CompositeDatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import javax.validation.ValidationException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * Юнит тестирование UserService
 * @see UserService
 * */

@SqlGroup({
		@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = {"classpath:city.sql", "classpath:users.sql" }),
		@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
})
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

	@Autowired
	private DataSource dataSource;

	/**
	 * Тестирование метода createNewUser()
	 * Сохранение записи в таблицу
	 * */
	@Test
	public void whenCreateNewUser_ThenReturnUserObject() {
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

	/**
	 * Тестирование метода createNewUser()
	 * Не удачная попытка сохранения записи в таблицу
	 * */

	@Test
	void whenCreateNewUser_ThenReturnThrowEmailException()
	{
		UserDTO dto = UserDTO.builder()
				.email("alex2000@mail.ru")
				.build();

		Mockito.doReturn(Optional.of(new User()))
				.when(userRepository)
				.findUserByEmail("alex2000@mail.ru");

		assertThrows(ValidationException.class, () -> userService.createNewUser(dto));

		Mockito.doReturn(new User())
				.when(userMapper)
				.toEntity(dto);

		Mockito.verify(userRepository, Mockito.times(0)).save(userMapper.toEntity(dto));
	}


	/**
	 * Тестирование метода findById(USER_ID)
	 * Получение записи из таблицы по ID
	 * */
	@Test
	public void whenFindUserById_ThenReturnUser() {
		User user = User.builder()
				.id(USER_ID)
				.email("akvamarin@gmail.com")
				.phone("89991210000")
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
				.phone("89991210000")
				.password("$2a$10$ZmJxIt7HaqxXqwpvd7scte0UmLndHSn2DgZVS99Ug0xZRck2rJ6fO")
				.nickname("Akvamarin")
				.dateOfBirthday(LocalDate.of(1995,3,3))
				.urlAvatar("http://********")
				.build();

		doReturn(Optional.of(user))
				.when(userRepository)
				.findById(USER_ID);

		//doReturn(expectedUserDTO).when(userMapper.toDTO(user));
		//Mockito.when(userMapper.toDTO(user)).thenReturn(expectedUserDTO);

		Assertions.assertTrue(userRepository.findById(USER_ID).isPresent());	//если существует объект
		var actualResult = userService.findById(USER_ID);

		Assertions.assertEquals(expectedUserDTO.getId(), actualResult.getId());
		Assertions.assertEquals(expectedUserDTO.getEmail(), actualResult.getEmail());

		Mockito.verify(userRepository, Mockito.times(1))		//ожидаемое количество вызовов
				.findById(USER_ID);
	}

	@Test
	public void whenFindUserByIdAndNotFoundUser_ThenReturnException() throws Exception {
		final User user = null;

		doReturn(Optional.ofNullable(user))
				.when(userRepository)
				.findById(NOT_FOUND_USER_ID);

		Assertions.assertThrows(EntityNotFoundException.class, () ->
			userService.findById(NOT_FOUND_USER_ID)
		);
	}

	/**
	 * Тестирование метода findAll()
	 * Получение всех записей из таблицы
	 * */
	@Test
	void whenFindAll_ThenReturnUsers()
	{
		Mockito.doReturn(new ArrayList<User>())
				.when(userRepository)
				.findAll();
		assertNotNull(userRepository.findAll());
		Mockito.verify(userRepository, Mockito.times(1)).findAll();
	}

	/**
	 * Тестирование метода updateUser()
	 * Обновление инфы о пользователе
	 * */
	@Test
	void whenUpdateUser_ThenReturnUsers() {
		UserDTO dto = UserDTO.builder()
				.id(USER_ID)
				.nickname("Akva")
				.aboutMe("Люблю путешествовать")
				.email("akvamarin@gmail.com")
				.phone("89991210000")
				.password("$2a$10$ZmJxIt7HaqxXqwpvd7scte0UmLndHSn2DgZVS99Ug0xZRck2rJ6fO")
				.dateOfBirthday(LocalDate.of(1995,3,3))
				.urlAvatar("http://********")
				.build();

		User expectedUser = User.builder()
				.id(USER_ID)
				.nickname("Akva")
				.aboutMe("Люблю путешествовать")
				.build();

		var actualResult = userService.updateUser(dto);

		assertNotNull(actualResult);
		Assertions.assertEquals(expectedUser.getId(), actualResult.getId());
		Assertions.assertEquals(expectedUser.getNickname(), actualResult.getNickname());
		Assertions.assertEquals(expectedUser.getAboutMe(), actualResult.getAboutMe());
	}

}
