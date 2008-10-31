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

project(REG_WRAP C)

# cmake setup boiler-plate
cmake_minimum_required(VERSION 2.6)
set(CMAKE_MODULE_PATH "${PROJECT_SOURCE_DIR}/CMake")

# set output directories, or at least hide them.
if(NOT LIBRARY_OUTPUT_PATH)
  set(LIBRARY_OUTPUT_PATH "" CACHE INTERNAL "Hide output directory option for building libraries.")
endif(NOT LIBRARY_OUTPUT_PATH)
mark_as_advanced(LIBRARY_OUTPUT_PATH)
if(NOT EXECUTABLE_OUTPUT_PATH)
  set(EXECUTABLE_OUTPUT_PATH ${REG_WRAP_BINARY_DIR}/bin CACHE INTERNAL "Single output directory for building all executables.")
endif(NOT EXECUTABLE_OUTPUT_PATH)

#
# find the RealityGrid Steering Library
#

find_package(RealityGrid REQUIRED)
include(${RealityGrid_USE_FILE})

#
# set up install location
#

# optionally install into the current ReG location
option(REG_WRAP_INSTALL_TO_REG_DIR
  "Install the wrappers to the same location as the RealityGrid installation that they are being built for?"
  OFF
)
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
