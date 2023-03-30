package com.akvamarin.friendsappserver.unittest;

import com.akvamarin.friendsappserver.domain.dto.AuthUserSocialDTO;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.entity.location.Country;
import com.akvamarin.friendsappserver.domain.enums.Role;
import com.akvamarin.friendsappserver.domain.enums.Sex;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.services.CityService;
import com.akvamarin.friendsappserver.services.UserService;
import com.akvamarin.friendsappserver.services.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Юнит тестирование UserService
 * @see UserService
 * */

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserMapper userMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private CityService cityService;

	@InjectMocks
	private UserServiceImpl userService;


	/**
	 * Тестирование метода createNewUser()
	 * Сохранение записи в таблицу
	 */
	@Test
	void createNewUser_withNonExistingEmail_shouldCreateUser() throws ValidationException {
		// given
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername("user@example.com");
		userDTO.setEmail("user@example.com");
		userDTO.setPassword("password");
		userDTO.setDateOfBirthday(LocalDate.parse("2000-01-02"));
		userDTO.setNickname("user");

		User user = new User();
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		user.setDateOfBirthday(userDTO.getDateOfBirthday());
		user.setNickname(userDTO.getNickname());

		Mockito.when(userRepository.findByEmail(userDTO.getEmail()))
				.thenReturn(Optional.empty());

		Mockito.when(userMapper.toEntity(userDTO))
				.thenReturn(user);

		Mockito.when(passwordEncoder.encode(userDTO.getPassword()))
				.thenReturn("hashed_password");

		Mockito.when(userRepository.save(user))
				.thenReturn(user);

		// when
		User result = userService.createNewUser(userDTO);

		// then
		Assertions.assertNotNull(result);
		Assertions.assertEquals(user.getEmail(), result.getEmail());
		Assertions.assertEquals(user.getPassword(), result.getPassword());
		Assertions.assertTrue(result.isEnabled());

		Mockito.verify(userRepository, Mockito.times(1)).findByEmail(userDTO.getEmail());
		Mockito.verify(userMapper, Mockito.times(1)).toEntity(userDTO);
		Mockito.verify(passwordEncoder, Mockito.times(1)).encode(userDTO.getPassword());
		Mockito.verify(userRepository, Mockito.times(1)).save(user);
	}

	@Test
	void createNewUser_withExistingEmail_shouldThrowValidationException() {
		// given
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername("user@example.com");
		userDTO.setEmail("user@example.com");
		userDTO.setPassword("password");
		userDTO.setDateOfBirthday(LocalDate.parse("2000-01-02"));
		userDTO.setNickname("user");

		Mockito.when(userRepository.findByEmail(userDTO.getEmail()))
				.thenReturn(Optional.of(new User()));

		// when, then
		Assertions.assertThrows(ValidationException.class, () -> userService.createNewUser(userDTO));

		Mockito.verify(userRepository, Mockito.times(1)).findByEmail(userDTO.getEmail());
	}

	/**
	 * Тестирование метода createNewUserVK()
	 * Сохранение записи в таблицу
	 */
	@Test
	void createNewUserVK_withNonExistingVkIdOrEmail_shouldCreateUser() throws ValidationException {
		// given
		AuthUserSocialDTO userDTO = AuthUserSocialDTO.builder()
				.username("vk@example.com")
				.email("vk@example.com")
				.vkId("12345678")
				.socialToken("user_vk_token")
				.dateOfBirth("2000-01-02")
				.sex(Sex.MALE)
				.city("Irkutsk")
				.country("Russia")
				.roles(Collections.singleton(Role.USER.name()))
				.build();

		Country country = Country.builder()
				.id(1L)
				.name(userDTO.getCountry())
				.build();

		City city = City.builder()
				.id(1L)
				.name(userDTO.getCity())
				.country(country)
				.build();

		User user = new User();
		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail());
		user.setVkId(userDTO.getVkId());
		user.setDateOfBirthday(LocalDate.parse(userDTO.getDateOfBirth()));
		user.setSex(userDTO.getSex());
		user.setCity(city);

		Mockito.when(userRepository.findByEmailOrVkId(userDTO.getEmail(), userDTO.getVkId()))
				.thenReturn(Optional.empty());

		Mockito.when(userMapper.toEntity(userDTO))
				.thenReturn(user);

		Mockito.when(cityService.findCityIfNoCreateNew(userDTO.getCity(), userDTO.getCountry()))
				.thenReturn(city);

		Mockito.when(userRepository.save(user))
				.thenReturn(user);

		// when
		User result = userService.createNewUserVK(userDTO);

		// then
		Assertions.assertNotNull(result);
		Assertions.assertEquals(user.getEmail(), result.getEmail());
		Assertions.assertEquals(user.getVkId(), result.getVkId());
		Assertions.assertEquals(user.getPassword(), result.getPassword());
		Assertions.assertTrue(result.isEnabled());

		Mockito.verify(userRepository, Mockito.times(1)).findByEmailOrVkId(userDTO.getEmail(), userDTO.getVkId());
		Mockito.verify(userMapper, Mockito.times(1)).toEntity(userDTO);
		Mockito.verify(cityService, Mockito.times(1)).findCityIfNoCreateNew(userDTO.getCity(), userDTO.getCountry());
		Mockito.verify(userRepository, Mockito.times(1)).save(user);
	}

	@Test
	void createNewUserVK_withExistingVkId_shouldThrowValidationException() {
		// given
		AuthUserSocialDTO userDTO = AuthUserSocialDTO.builder()
				.username("vk@example.com")
				.email("vk@example.com")
				.vkId("12345678")
				.socialToken("user_vk_token")
				.dateOfBirth("2000-01-02")
				.sex(Sex.MALE)
				.city("Irkutsk")
				.country("Russia")
				.roles(Collections.singleton(Role.USER.name()))
				.build();

		Mockito.when(userRepository.findByEmailOrVkId(userDTO.getEmail(), userDTO.getVkId()))
				.thenReturn(Optional.of(new User()));

		// when, then
		Assertions.assertThrows(ValidationException.class, () -> userService.createNewUserVK(userDTO));

		Mockito.verify(userRepository, Mockito.times(1)).findByEmailOrVkId(userDTO.getEmail(), userDTO.getVkId());
	}

	/**
	 * Тестирование метода findAll()
	 * Получение всех пользователей
	 */
	@Test
	void findAll_shouldReturnListOfUserDTOs() {
		// given
		User user1 = User.builder()
				.id(1L)
				.nickname("user1")
				.build();

		User user2 = User.builder()
				.id(2L)
				.nickname("user2")
				.build();

		List<User> userList = Arrays.asList(user1, user2);

		UserDTO userDto1 = UserDTO.builder()
				.id(1L)
				.nickname("user1")
				.build();

		UserDTO userDto2 = UserDTO.builder()
				.id(2L)
				.nickname("user2")
				.build();

		List<UserDTO> expectedList = Arrays.asList(userDto1, userDto2);

		Mockito.when(userRepository.findAll())
				.thenReturn(userList);

		Mockito.when(userMapper.toDTO(user1))
				.thenReturn(userDto1);

		Mockito.when(userMapper.toDTO(user2))
				.thenReturn(userDto2);

		// when
		List<UserDTO> actualList = userService.findAll();

		// then
		Assertions.assertEquals(expectedList.size(), actualList.size());
		Assertions.assertEquals(expectedList.get(0).getId(), actualList.get(0).getId());
		Assertions.assertEquals(expectedList.get(0).getNickname(), actualList.get(0).getNickname());
		Assertions.assertEquals(expectedList.get(1).getId(), actualList.get(1).getId());
		Assertions.assertEquals(expectedList.get(1).getNickname(), actualList.get(1).getNickname());

		Mockito.verify(userRepository, Mockito.times(1)).findAll();
		Mockito.verify(userMapper, Mockito.times(1)).toDTO(user1);
		Mockito.verify(userMapper, Mockito.times(1)).toDTO(user2);

		log.info("Method findAll shouldReturnListOfUserDTOs works correctly");
	}

	/**
	 * Тестирование метода findById(USER_ID)
	 * Получение записи из таблицы по ID
	 */
	@Test
	void findById_shouldReturnUserDTO_whenUserExists() {
		// given
		long userId = 1L;

		User user = new User();
		user.setId(userId);
		user.setNickname("test_user");

		UserDTO expectedDto = new UserDTO();
		expectedDto.setId(userId);
		expectedDto.setNickname("test_user");

		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		Mockito.when(userMapper.toDTO(user)).thenReturn(expectedDto);

		// when
		UserDTO actualDto = userService.findById(userId);

		// then
		assertThat(actualDto).isEqualTo(expectedDto);
		Mockito.verify(userRepository).findById(userId);
		Mockito.verify(userMapper).toDTO(user);
	}

	@Test
	void findById_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {
		// given
		long userId = 1L;
		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> userService.findById(userId))
				.isInstanceOf(EntityNotFoundException.class)
				.hasMessage("User with ID 1 not found");
		Mockito.verify(userRepository).findById(userId);
	}

	/**
	 * Тестирование метода updateUser()
	 * Обновление инфо о пользователе
	 */
	@Test
	void updateUser_shouldUpdateUserAndReturnUpdatedUser() {
		// given
		UserDTO userDTO = new UserDTO();
		userDTO.setId(1L);
		userDTO.setNickname("updatedNickname");

		User existingUser = new User();
		existingUser.setId(1L);
		existingUser.setNickname("oldNickname");

		Mockito.when(userRepository.findById(userDTO.getId()))
				.thenReturn(Optional.of(existingUser));

		//используется для присвоения nickname для existingUser
		//для того, чтобы иммитировать возврат existingUser из userRepository
		//т.к. updateEntity маппер ничего не возвращает
		Mockito.doAnswer(invocation -> {
			UserDTO argUserDTO = invocation.getArgument(0);
			User argUser = invocation.getArgument(1);
			argUser.setNickname(argUserDTO.getNickname());
			return null;
		}).when(userMapper).updateEntity(userDTO, existingUser);

		Mockito.when(userRepository.save(existingUser))
				.thenReturn(existingUser);

		// when
		User result = userService.updateUser(userDTO);

		// then
		assertEquals(existingUser, result);
		assertEquals(userDTO.getNickname(), existingUser.getNickname());
		Mockito.verify(userRepository).findById(userDTO.getId());
		Mockito.verify(userMapper).updateEntity(userDTO, existingUser);
		Mockito.verify(userRepository).save(existingUser);
	}

	/**
	 * Тестирование метода updateUser()
	 * Когда пользователь для обновления не найден
	 * вернуть EntityNotFoundException
	 */
	@Test
	void updateUser_shouldThrowEntityNotFoundExceptionIfUserNotFound() {
		// given
		UserDTO userDTO = new UserDTO();
		userDTO.setId(1L);
		userDTO.setNickname("updatedNickname");

		when(userRepository.findById(userDTO.getId())).thenReturn(java.util.Optional.empty());

		// when, then
		assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userDTO));
		verify(userRepository, times(1)).findById(userDTO.getId());
		verifyNoMoreInteractions(userRepository, userMapper, passwordEncoder);
	}


	/**
	 * Тестирование метода deleteUser()
	 * Удаление аккаунта пользователя из БД
	 * */
	@Test
	void deleteById_shouldDeleteExistingUserAndReturnTrue() {
		// given
		User user = new User();
		user.setId(1L);

		Mockito.when(userRepository.findById(1L))
				.thenReturn(Optional.of(user));

		// when
		boolean isDeleted = userService.deleteById(1L);

		// then
		assertTrue(isDeleted);
		Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
	}

	@Test
	void deleteById_shouldThrowEntityNotFoundExceptionForNonExistingUser() {
		// given
		Mockito.when(userRepository.findById(1L))
				.thenReturn(Optional.empty());

		// when, then
		EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userService.deleteById(1L));
		assertEquals("User with ID 1 not found", ex.getMessage());
	}

}
