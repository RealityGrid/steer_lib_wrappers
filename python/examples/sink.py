#!/usr/bin/env python
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

from ReG_Steer import *
import sys, time

# Some global variables
nloops = 5000

finished = 0
status = 0
sleep_time = 1

# init and enable the steering library

Steering_enable(REG_TRUE)

status = Steering_initialize("Python Sink v1.0", [REG_STR_STOP])

if status != REG_SUCCESS:
    print "Steering library initialization failed"
    sys.exit(0)

# Register IO channel

status, iotype_handle = Register_IOType("VTK_STRUCTURED_POINTS", REG_IO_IN, 1)

if status != REG_SUCCESS:
    print "Failed to register IO type"
    sys.exit(0)

# Register param(s)

bytes_read = intp()
steer_test = floatp()
status = Register_param("Bytes_read", REG_FALSE, bytes_read, REG_INT, "", "")
status = Register_param("Bytes_read", REG_TRUE, steer_test, REG_FLOAT, "-10.0", "10.0")
steer_test.assign(5.0)

# main loop waiting for data
i = 0
while i < nloops:
    time.sleep(sleep_time)
    print "\ni = %d\nTEST_STEER = %f" % (i, steer_test.value())
    status, changed_param_labels, recvd_cmds, recvd_cmd_params = Steering_control(i)
    num_params_changed = len(changed_param_labels)
    num_recvd_cmds = len(recvd_cmds)

    if status != REG_SUCCESS:
        continue

    # zero count of bytes read this time
    bytes_read.assign(0)

    if num_recvd_cmds > 0:
        for icmd in range(0, num_recvd_cmds):
            if recvd_cmds[icmd] == REG_STR_STOP:
                finished = 1
                break
            else:
                if recvd_cmds[icmd] == iotype_handle:
                    status, iohandle = Consume_start(iotype_handle)
                    if status == REG_SUCCESS:
                        status, data_type, data_count = Consume_data_slice_header(iohandle)
                        while status == REG_SUCCESS:
                            print "Got data: type = %d, count = %d" % (data_type, data_count)
                            status, outdata = Consume_data_slice(iohandle,
                                                                 data_type, 
                                                                 data_count)
                            bytes_read.assign(bytes_read.value()
                                              + (data_count * Sizeof(data_type)))
                            
                            status, data_type, data_count = Consume_data_slice_header(iohandle)
                    status, iohandle = Consume_stop(iohandle)
                break
            if finished == 1:
                break
        if finished == 1:
            break
    
    i = i + 1
    
status = Steering_finalize()
