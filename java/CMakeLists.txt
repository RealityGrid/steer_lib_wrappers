#
#  The RealityGrid Steering Library Wrappers
#
#  Copyright (c) 2002-2009, University of Manchester, United Kingdom.
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

# Java compile stuff we have overridden to compensate for CMake bugs
set(CMAKE_USER_MAKE_RULES_OVERRIDE "JavaCompileOverride")

enable_language(Java)
find_package(JNI REQUIRED)
include_directories(${JAVA_INCLUDE_PATH} ${JAVA_INCLUDE_PATH2})

if(REG_WRAP_RE_SWIG)
  file(MAKE_DIRECTORY "${CMAKE_CURRENT_BINARY_DIR}/org/realitygrid/steering")
  set(CMAKE_SWIG_OUTDIR "${CMAKE_CURRENT_BINARY_DIR}/org/realitygrid/steering")
  set(CMAKE_SWIG_FLAGS -package org.realitygrid.steering)
  SWIG_ADD_MODULE(ReG_SteerJava java ${CMAKE_CURRENT_SOURCE_DIR}/ReG_Steer.i)
  SWIG_LINK_LIBRARIES(ReG_SteerJava ${REG_LINK_LIBRARIES} ${REG_EXTERNAL_LIBS})
  set_target_properties(${SWIG_MODULE_ReG_SteerJava_REAL_NAME}
    PROPERTIES OUTPUT_NAME libReG_SteerJava
  )
else(REG_WRAP_RE_SWIG)
  set(REG_WRAP_JAVA_PREGEN_SRCS generated/ReG_Steer_wrap.c)
  add_library(ReG_SteerJava SHARED ${REG_WRAP_JAVA_PREGEN_SRCS})
  target_link_libraries(ReG_SteerJava
    ${REG_LINK_LIBRARIES} ${REG_EXTERNAL_LIBS}
  )
  include_directories(${CMAKE_CURRENT_SOURCE_DIR}/generated)
endif(REG_WRAP_RE_SWIG)

# build all the java sources and build a jar
file(GLOB_RECURSE REG_JAVA_FILES ${CMAKE_CURRENT_SOURCE_DIR}/org/*.java)
include_directories(
  ${CMAKE_CURRENT_SOURCE_DIR}
  ${CMAKE_CURRENT_BINARY_DIR}
)
add_library(ReG_Steer_Jar ${REG_JAVA_FILES})
add_dependencies(ReG_Steer_Jar libReG_SteerJava)
set_target_properties(ReG_Steer_Jar PROPERTIES OUTPUT_NAME ReG_Steer)

# build the docs?
option(REG_WRAP_BUILD_JAVADOC "Build the Javadoc documentation for the generated classes?" OFF)
if(REG_WRAP_BUILD_JAVADOC)
  add_subdirectory(doc)
endif(REG_WRAP_BUILD_JAVADOC)

# install rules
set(REG_WRAP_JAVA_INSTALL ${REG_WRAP_LIB_INSTALL}/java)
install(TARGETS ReG_SteerJava ReG_Steer_Jar
  LIBRARY DESTINATION ${REG_WRAP_JAVA_INSTALL}
  ARCHIVE DESTINATION ${REG_WRAP_JAVA_INSTALL}
)
