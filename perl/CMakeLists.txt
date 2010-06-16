#
#  The RealityGrid Steering Library Wrappers
#
#  Copyright (c) 2002-2010, University of Manchester, United Kingdom.
#  All rights reserved.
#
#  This software is produced by Research Computing Services, University
#  of Manchester as part of the RealityGrid project and associated
#  follow on projects, funded by the EPSRC under grants GR/R67699/01,
#  GR/R67699/02, GR/T27488/01, EP/C536452/1, EP/D500028/1,
#  EP/F00561X/1.
#
#  LICENCE TERMS
#
#  Redistribution and use in source and binary forms, with or without
#  modification, are permitted provided that the following conditions
#  are met:
#
#    * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#
#    * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#      with the distribution.
#
#    * Neither the name of The University of Manchester nor the names
#      of its contributors may be used to endorse or promote products
#      derived from this software without specific prior written
#      permission.
#
#  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
#  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
#  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
#  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
#  COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
#  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
#  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
#  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
#  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
#  LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
#  ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
#  POSSIBILITY OF SUCH DAMAGE.
#
#  Author: Robert Haines

find_package(PerlLibs REQUIRED)
include_directories(${PERL_INCLUDE_PATH})

if(REG_WRAP_RE_SWIG)
  set(CMAKE_SWIG_FLAGS -proxy -exportall)
  SWIG_ADD_MODULE(ReG_Steer_Perl perl ${CMAKE_CURRENT_SOURCE_DIR}/ReG_Steer.i)
  if(NOT ${PERL_EXTRA_C_FLAGS} STREQUAL "\n")
    set_source_files_properties(ReG_SteerPERL_wrap.c
      PROPERTIES COMPILE_FLAGS ${PERL_EXTRA_C_FLAGS}
    )
  endif(NOT ${PERL_EXTRA_C_FLAGS} STREQUAL "\n")
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
  if(NOT ${PERL_EXTRA_C_FLAGS} STREQUAL "\n")
    set_source_files_properties(${REG_WRAP_PERL_PREGEN_SRCS}
      PROPERTIES COMPILE_FLAGS ${PERL_EXTRA_C_FLAGS}
    )
  endif(NOT ${PERL_EXTRA_C_FLAGS} STREQUAL "\n")
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
set(WRAPPERS_PERL_INSTALL ${WRAPPERS_LIB_INSTALL}/perl)
install(TARGETS ReG_Steer_Perl LIBRARY DESTINATION ${WRAPPERS_PERL_INSTALL})
install(FILES ${CMAKE_CURRENT_BINARY_DIR}/ReG_Steer.pm DESTINATION ${WRAPPERS_PERL_INSTALL})
