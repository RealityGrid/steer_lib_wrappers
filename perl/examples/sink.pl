#!/usr/bin/env perl
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

use ReG_Steer;
use strict;

# Some global variables
my $nloops = 5000;

my $finished = 0;
my $status = 0;
my $sleep_time = 1;

# init and enable the steering library
#&ReG_Steer::Steering_enable($ReG_Steer::REG_TRUE);
&Steering_enable($REG_TRUE);

my @cmds = ($REG_STR_STOP);
$status = &Steering_initialize("Perl Sink v1.0", \@cmds);
if($status != $REG_SUCCESS) {
    die "Steering library initialization failed\n";
}

# Register IO channel
my $iotype_handle;
($status, $iotype_handle) = &Register_IOType("VTK_STRUCTURED_POINTS", $REG_IO_IN, 1);
if($status != $REG_SUCCESS) {
    die "Failed to register IO type\n";
}

# Register param(s)
my $bytes_read = new ReG_Steer::intp();
my $test_steer = new ReG_Steer::floatp();
$status = &Register_param("Bytes_read", $REG_FALSE, $bytes_read, $REG_INT, "", "");
$status = &Register_param("Test_Steer", $REG_TRUE, $test_steer, $REG_FLOAT, "-10.0", "10.0");
if($status != $REG_SUCCESS) {
    die "Failed to register parameter(s)\n";
}
$test_steer->assign(5.0);

# main loop waiting for data
for(my $i = 0; $i < $nloops; $i++) {
    sleep $sleep_time;
    print "\ni = $i\nTEST_STEER = ",$test_steer->value(),"\n";
    my ($changed_param_labels, $recvd_cmds, $recvd_cmd_params);
    ($status, $changed_param_labels, $recvd_cmds, $recvd_cmd_params) = &Steering_control($i);
    if($status != $REG_SUCCESS) {
	next;
    }
    my $num_params_changed = @$changed_param_labels;
    my $num_recvd_cmds = @$recvd_cmds;

    # zero count of bytes read this time
    $bytes_read->assign(0);

    if($num_recvd_cmds > 0) {
	for(my $icmd = 0; $icmd < $num_recvd_cmds; $icmd++) {
	    if(@$recvd_cmds[$icmd] == $REG_STR_STOP) {
		$finished = 1;
		last;
	    }
	    else {
		if(@$recvd_cmds[$icmd] == $iotype_handle) {
		    my $iohandle;
		    ($status, $iohandle) = &Consume_start($iotype_handle);
		    if($status == $REG_SUCCESS) {
			my ($data_type, $data_count);
			($status, $data_type, $data_count) = &Consume_data_slice_header($iohandle);
			while($status == $REG_SUCCESS) {
			    print "Got data: type = $data_type, count = $data_count\n";
			    my $outdata;
			    ($status, $outdata) = &Consume_data_slice($iohandle,
								      $data_type,
								      $data_count);
			    if($data_type == $REG_CHAR) {
				print $$outdata,"\n";
			    }
			    $bytes_read->assign($bytes_read->value() +
						($data_count * &Sizeof($data_type)));

			    ($status, $data_type, $data_count) = &Consume_data_slice_header($iohandle);
			} # while REG_SUCCESS
		    }
		    ($status, $iohandle) = &Consume_stop($iohandle);
		} #
		last;
	    } # if recvd_cmds[icmd]
	    if($finished == 1) {
		last;
	    }
	} # for icmd
	if($finished == 1) {
	    last;
	}
    } # if num_recvd_cmds > 0
} # for nloops

$status = &Steering_finalize();
