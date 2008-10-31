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

find_package(Javadoc)

# variables to drive the process
set(REG_WRAP_JAVADOC_OUTPUT_PATH "${CMAKE_BINARY_DIR}/javadoc")
set(REG_WRAP_JAVADOC_SRCPATH ${CMAKE_CURRENT_SOURCE_DIR}/..:${CMAKE_CURRENT_BINARY_DIR}/..)
if(NOT REG_WRAP_RE_SWIG)
  set(REG_WRAP_JAVADOC_SRCPATH ${REG_WRAP_JAVADOC_SRCPATH}:${CMAKE_CURRENT_SOURCE_DIR}/../generated)
endif(NOT REG_WRAP_RE_SWIG)
set(REG_WRAP_JAVADOC_OVERVIEW "${CMAKE_CURRENT_SOURCE_DIR}/../org/realitygrid/overview.html")

file(MAKE_DIRECTORY "${REG_WRAP_JAVADOC_OUTPUT_PATH}")

add_custom_target(ReG_SteerJavadoc ALL
  ${JAVA_JAVADOC}
  -d "${REG_WRAP_JAVADOC_OUTPUT_PATH}"
  -windowtitle \"RealityGrid Java API\"
  -doctitle \"RealityGrid Java API\"
  -header \"<strong>RealityGrid<br>Java API</strong>\"
  -bottom \"\(C\) Copyright 2004-2008 The University of Manchester, Manchester, United Kingdom. All rights reserved.\"
  -link \"http://java.sun.com/j2se/1.4.2/docs/api\"
  -overview ${CMAKE_CURRENT_SOURCE_DIR}/overview.html
  -breakiterator
  -author
  -version
  -sourcepath "${REG_WRAP_JAVADOC_SRCPATH}"
  -group \"Core RealityGrid Packages\" \"org.realitygrid.steering\"
  -group \"RealityGrid Extension Packages\" \"org.realitygrid.utilities\"
  -subpackages org
  -quiet
)

# install rules
install(DIRECTORY "${REG_WRAP_JAVADOC_OUTPUT_PATH}" DESTINATION ${REG_WRAP_DOC_INSTALL})
