package swp.happyprogramming.adapter.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import swp.happyprogramming.application.port.out.AddressPortOut;
import swp.happyprogramming.application.port.out.CarePortOut;
import swp.happyprogramming.application.port.out.ConnectPortOut;
import swp.happyprogramming.application.port.out.DistrictPortOut;
import swp.happyprogramming.application.port.out.ExperiencePortOut;
import swp.happyprogramming.application.port.out.FeedbackPortOut;
import swp.happyprogramming.application.port.out.MentorPortOut;
import swp.happyprogramming.application.port.out.MessagePortOut;
import swp.happyprogramming.application.port.out.MethodPortOut;
import swp.happyprogramming.application.port.out.NotificationPortOut;
import swp.happyprogramming.application.port.out.PostPortOut;
import swp.happyprogramming.application.port.out.ProvincePortOut;
import swp.happyprogramming.application.port.out.RequestPortOut;
import swp.happyprogramming.application.port.out.RolePortOut;
import swp.happyprogramming.application.port.out.SkillPortOut;
import swp.happyprogramming.application.port.out.UserPortOut;
import swp.happyprogramming.application.port.out.WardPortOut;
import swp.happyprogramming.application.port.usecase.IAddressService;
import swp.happyprogramming.application.port.usecase.IConnectService;
import swp.happyprogramming.application.port.usecase.IExperienceService;
import swp.happyprogramming.application.port.usecase.IFeedbackService;
import swp.happyprogramming.application.port.usecase.IMessageService;
import swp.happyprogramming.application.port.usecase.INotificationService;
import swp.happyprogramming.application.port.usecase.IPostService;
import swp.happyprogramming.application.port.usecase.IRequestService;
import swp.happyprogramming.application.port.usecase.ISkillService;
import swp.happyprogramming.application.port.usecase.IUserService;
import swp.happyprogramming.application.port.usecase.MentorUseCase;
import swp.happyprogramming.application.services.AddressService;
import swp.happyprogramming.application.services.ConnectService;
import swp.happyprogramming.application.services.ExperienceService;
import swp.happyprogramming.application.services.FeedbackService;
import swp.happyprogramming.application.services.MentorService;
import swp.happyprogramming.application.services.MessageService;
import swp.happyprogramming.application.services.NotificationService;
import swp.happyprogramming.application.services.PostService;
import swp.happyprogramming.application.services.RequestService;
import swp.happyprogramming.application.services.SkillService;
import swp.happyprogramming.application.services.UserService;

@Configuration
public class SpringAppConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    return modelMapper;
  }

  //  AddressService bean method
  @Bean
  public IAddressService addressService(AddressPortOut addressRepository,
    WardPortOut wardRepository, DistrictPortOut districtRepository,
    ProvincePortOut provinceRepository) {
    return new AddressService(addressRepository, wardRepository,
      districtRepository, provinceRepository);
  }

  //  ConnectService bean method
  @Bean
  public IConnectService connectService(ConnectPortOut connectRepository) {
    return new ConnectService(connectRepository);
  }

  //  ExperienceService bean method
  @Bean
  public IExperienceService experienceService(
    ExperiencePortOut experienceRepository) {
    return new ExperienceService(experienceRepository);
  }

  //  FeedbackService bean method
  @Bean
  public IFeedbackService feedbackService(FeedbackPortOut feedbackRepository) {
    return new FeedbackService(feedbackRepository);
  }

  //  MentorService bean method
  @Bean
  public MentorUseCase mentorService(MentorPortOut mentorRepository,
    ModelMapper modelMapper, IUserService userService,
    IAddressService addressService) {
    return new MentorService(mentorRepository, modelMapper, userService,
      addressService);
  }

  //  MessageService bean method
  @Bean
  public IMessageService messageService(MessagePortOut messageRepository) {
    return new MessageService(messageRepository);
  }

  //  NotificationService bean method
  @Bean
  public INotificationService notificationService(
    NotificationPortOut notificationRepository) {
    return new NotificationService(notificationRepository);
  }

  //  PostService bean method
  @Bean
  public IPostService postService(ModelMapper modelMapper,
    PostPortOut postRepository, MethodPortOut methodPortOut,
    UserPortOut userRepository) {
    return new PostService(modelMapper, postRepository, methodPortOut,
      userRepository);
  }

  //  RequestService bean method
  @Bean
  public IRequestService requestService(RequestPortOut requestRepository,
    ConnectPortOut connectRepository) {
    return new RequestService(requestRepository, connectRepository);
  }

  @Bean
  public ISkillService skillService(SkillPortOut skillRepository) {
    return new SkillService(skillRepository);
  }

  @Bean
  public IUserService userService(ModelMapper modelMapper,
    MethodPortOut methodPortOut, UserPortOut userRepository,
    IAddressService addressRepository,
    BCryptPasswordEncoder bCryptPasswordEncoder, CarePortOut carePortOut,
    RolePortOut rolePortOut) {
    return new UserService(modelMapper, methodPortOut, userRepository,
      addressRepository, bCryptPasswordEncoder, carePortOut, rolePortOut);
  }

}
