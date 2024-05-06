package com.conference.config;

import com.conference.dto.ConferenceRoomDTO;
import com.conference.entity.ConferenceRoom;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
  @Bean
  public ModelMapper getModelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    Converter<ConferenceRoom, ConferenceRoomDTO.ConferenceRoomDTOBuilder> userConverter =
        ctx -> {
          ConferenceRoomDTO.ConferenceRoomDTOBuilder builder = ConferenceRoomDTO.builder();
          builder.id(ctx.getSource().getId());
          builder.roomName(ctx.getSource().getRoomName());
          builder.roomCapacity(ctx.getSource().getRoomCapacity());
          builder.locationId(ctx.getSource().getLocationId());
          return builder;
        };
    modelMapper
        .typeMap(ConferenceRoom.class, ConferenceRoomDTO.ConferenceRoomDTOBuilder.class)
        .setConverter(userConverter);
    return modelMapper;
  }
}
