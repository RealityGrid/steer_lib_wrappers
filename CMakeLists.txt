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

project(REG_WRAP)

# cmake setup boiler-plate
cmake_minimum_required(VERSION 2.6)
set(CMAKE_MODULE_PATH "${PROJECT_SOURCE_DIR}/CMake")

# version numbers
set(REG_WRAP_VERSION_MAJ 3)
set(REG_WRAP_VERSION_MIN 0)
set(REG_WRAP_VERSION_PAT 1)
set(REG_WRAP_VERSION
    "${REG_WRAP_VERSION_MAJ}.${REG_WRAP_VERSION_MIN}.${REG_WRAP_VERSION_PAT}")

# set output directories, or at least hide them.
if(NOT LIBRARY_OUTPUT_PATH)
  set(LIBRARY_OUTPUT_PATH "" CACHE INTERNAL "Hide output directory option for building libraries.")
endif(NOT LIBRARY_OUTPUT_PATH)
mark_as_advanced(LIBRARY_OUTPUT_PATH)
if(NOT EXECUTABLE_OUTPUT_PATH)
  set(EXECUTABLE_OUTPUT_PATH ${REG_WRAP_BINARY_DIR}/bin CACHE INTERNAL "Single output directory for building all executables.")
endif(NOT EXECUTABLE_OUTPUT_PATH)

#
# find the RealityGrid Steering Library and check version
#

find_package(RealityGrid REQUIRED)
include(${RealityGrid_USE_FILE})

reg_check_version("Wrappers" ${REG_WRAP_VERSION_MAJ})

#
# set up install location
#

# optionally install into the current ReG location
# if not building against a build tree install
if(${RealityGrid_INSTALL_TYPE} MATCHES "install")
  option(REG_WRAP_INSTALL_TO_REG_DIR
    "Install the wrappers to the same location as the RealityGrid installation that they are being built for?"
    OFF
  )
endif(${RealityGrid_INSTALL_TYPE} MATCHES "install")

if(REG_WRAP_INSTALL_TO_REG_DIR)
  set(CMAKE_INSTALL_PREFIX ${RealityGrid_INSTALL_PREFIX}
    CACHE PATH
    "Install path prefix, prepended onto install directories. The default setting is to install the wrappers to the same location as the RealityGrid installation that they are being built for."
    FORCE
  )

  string(
    REPLACE "${RealityGrid_INSTALL_PREFIX}/" "" REG_WRAP_LIB_INSTALL
    ${RealityGrid_LIBRARY_DIRS}
  )
  string(
    REPLACE "${RealityGrid_INSTALL_PREFIX}/" "" REG_WRAP_DOC_INSTALL
    ${RealityGrid_DOCS_DIR}
  )
else(REG_WRAP_INSTALL_TO_REG_DIR)
  set(REG_WRAP_LIB_INSTALL "lib/RealityGrid")
  set(REG_WRAP_DOC_INSTALL "share/doc/RealityGrid")
endif(REG_WRAP_INSTALL_TO_REG_DIR)

#
# rebuild the swig wrappers from scratch?
# if not, simply use the provided pre-generated ones
#

option(REG_WRAP_RE_SWIG "Rebuild the SWIG wrappers from scratch? If not the pre-generated ones will be used. Only turn this on if you have SWIG installed and the pregenerated bindings are not working for you!" OFF)
mark_as_advanced(REG_WRAP_RE_SWIG)

if(REG_WRAP_RE_SWIG)
  find_package(SWIG REQUIRED)
  include(${SWIG_USE_FILE})

  # to find ReG_Steer_API.i
  include_directories(${CMAKE_SOURCE_DIR})
endif(REG_WRAP_RE_SWIG)

#
# which wrappers to build?
#

option(REG_WRAP_JAVA "Wrap the steering library for Java" OFF)
if(REG_WRAP_JAVA)
  add_subdirectory(java)
endif(REG_WRAP_JAVA)

option(REG_WRAP_PERL "Wrap the steering library for Perl" OFF)
if(REG_WRAP_PERL)
  add_subdirectory(perl)
endif(REG_WRAP_PERL)

option(REG_WRAP_PYTHON "Wrap the steering library for Python" OFF)
if(REG_WRAP_PYTHON)
  add_subdirectory(python)
endif(REG_WRAP_PYTHON)
