#
#  CMake build machinery for the RealityGrid Steering Library Wrappers.
#
#  (C) Copyright 2004-2008, University of Manchester, United Kingdom,
#  all rights reserved.
#
#  This software is produced by the Supercomputing, Visualization and
#  e-Science Group, Manchester Computing, University of Manchester
#  as part of the RealityGrid project (http://www.realitygrid.org),
#  funded by the EPSRC under grants GR/R67699/01 and GR/R67699/02.
#
#  LICENCE TERMS
#
#  Redistribution and use in source and binary forms, with or without
#  modification, are permitted provided that the following conditions
#  are met:
#  1. Redistributions of source code must retain the above copyright
#     notice, this list of conditions and the following disclaimer.
#  2. Redistributions in binary form must reproduce the above copyright
#     notice, this list of conditions and the following disclaimer in the
#     documentation and/or other materials provided with the distribution.
#
#  THIS MATERIAL IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
#  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
#  LIMITED  TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
#  A PARTICULAR PURPOSE ARE DISCLAIMED. THE ENTIRE RISK AS TO THE QUALITY
#  AND PERFORMANCE OF THE PROGRAM IS WITH YOU.  SHOULD THE PROGRAM PROVE
#  DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR
#  CORRECTION.
#
#  Author.........: Robert Haines
#----------------------------------------------------------------------

find_package(PerlLibs REQUIRED)
include_directories(${PERL_INCLUDE_PATH})

if(REG_WRAP_RE_SWIG)
  set(CMAKE_SWIG_FLAGS -proxy -exportall)
  SWIG_ADD_MODULE(ReG_Steer_Perl perl ${CMAKE_CURRENT_SOURCE_DIR}/ReG_Steer.i)
  set_source_files_properties(ReG_SteerPERL_wrap.c
    PROPERTIES COMPILE_FLAGS ${PERL_EXTRA_C_FLAGS}
  )
  SWIG_LINK_LIBRARIES(ReG_Steer_Perl
    ${REG_LINK_LIBRARIES} ${REG_EXTERNAL_LIBS} ${PERL_LIBRARY}
  )
  set_target_properties(${SWIG_MODULE_ReG_Steer_Perl_REAL_NAME}
    PROPERTIES OUTPUT_NAME ReG_Steer
  )
else(REG_WRAP_RE_SWIG)
  set(REG_WRAP_PERL_PREGEN_SRCS generated/ReG_Steer_wrap.c)
  add_library(ReG_Steer_Perl SHARED ${REG_WRAP_PERL_PREGEN_SRCS})
  target_link_libraries(ReG_Steer_Perl
    ${REG_LINK_LIBRARIES} ${REG_EXTERNAL_LIBS} ${PERL_LIBRARY}
  )
  set_source_files_properties(${REG_WRAP_PERL_PREGEN_SRCS}
    PROPERTIES COMPILE_FLAGS ${PERL_EXTRA_C_FLAGS}
  )
  set_target_properties(ReG_Steer_Perl PROPERTIES
    PREFIX ""
    OUTPUT_NAME ReG_Steer
  )
  configure_file(
    ${CMAKE_CURRENT_SOURCE_DIR}/generated/ReG_Steer.pm
    ${CMAKE_CURRENT_BINARY_DIR}/ReG_Steer.pm
    COPYONLY
  )
endif(REG_WRAP_RE_SWIG)

# install rules
set(REG_WRAP_PERL_INSTALL ${REG_WRAP_LIB_INSTALL}/perl)
install(TARGETS ReG_Steer_Perl LIBRARY DESTINATION ${REG_WRAP_PERL_INSTALL})
install(FILES ${CMAKE_CURRENT_BINARY_DIR}/ReG_Steer.pm DESTINATION ${REG_WRAP_PERL_INSTALL})
