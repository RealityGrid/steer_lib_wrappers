# This file was created automatically by SWIG 1.3.27.
# Don't modify this file, modify the SWIG interface instead.

import _ReG_Steer

# This file is compatible with both classic and new-style classes.
def _swig_setattr_nondynamic(self,class_type,name,value,static=1):
    if (name == "this"):
        if isinstance(value, class_type):
            self.__dict__[name] = value.this
            if hasattr(value,"thisown"): self.__dict__["thisown"] = value.thisown
            del value.thisown
            return
    method = class_type.__swig_setmethods__.get(name,None)
    if method: return method(self,value)
    if (not static) or hasattr(self,name) or (name == "thisown"):
        self.__dict__[name] = value
    else:
        raise AttributeError("You cannot add attributes to %s" % self)

def _swig_setattr(self,class_type,name,value):
    return _swig_setattr_nondynamic(self,class_type,name,value,0)

def _swig_getattr(self,class_type,name):
    method = class_type.__swig_getmethods__.get(name,None)
    if method: return method(self)
    raise AttributeError,name

import types
try:
    _object = types.ObjectType
    _newclass = 1
except AttributeError:
    class _object : pass
    _newclass = 0
del types


REG_STEER_NAMESPACE = _ReG_Steer.REG_STEER_NAMESPACE
APP_STEERABLE_FILENAME = _ReG_Steer.APP_STEERABLE_FILENAME
STR_CONNECTED_FILENAME = _ReG_Steer.STR_CONNECTED_FILENAME
APP_TO_STR_FILENAME = _ReG_Steer.APP_TO_STR_FILENAME
STR_TO_APP_FILENAME = _ReG_Steer.STR_TO_APP_FILENAME
REG_SUCCESS = _ReG_Steer.REG_SUCCESS
REG_FAILURE = _ReG_Steer.REG_FAILURE
REG_EOD = _ReG_Steer.REG_EOD
REG_MEM_FAIL = _ReG_Steer.REG_MEM_FAIL
REG_TIMED_OUT = _ReG_Steer.REG_TIMED_OUT
REG_NOT_READY = _ReG_Steer.REG_NOT_READY
REG_UNFINISHED = _ReG_Steer.REG_UNFINISHED
REG_MAX_NUM_STR_CMDS = _ReG_Steer.REG_MAX_NUM_STR_CMDS
REG_MAX_NUM_STR_PARAMS = _ReG_Steer.REG_MAX_NUM_STR_PARAMS
REG_MAX_NUM_LOG_MSG = _ReG_Steer.REG_MAX_NUM_LOG_MSG
REG_INITIAL_NUM_IOTYPES = _ReG_Steer.REG_INITIAL_NUM_IOTYPES
REG_INITIAL_NUM_PARAMS = _ReG_Steer.REG_INITIAL_NUM_PARAMS
REG_INITIAL_NUM_CMDS = _ReG_Steer.REG_INITIAL_NUM_CMDS
REG_MAX_NUM_STEERED_SIM = _ReG_Steer.REG_MAX_NUM_STEERED_SIM
REG_MAX_NUM_FILES = _ReG_Steer.REG_MAX_NUM_FILES
REG_MAX_STRING_LENGTH = _ReG_Steer.REG_MAX_STRING_LENGTH
REG_STR_STOP = _ReG_Steer.REG_STR_STOP
REG_STR_PAUSE = _ReG_Steer.REG_STR_PAUSE
REG_STR_RESUME = _ReG_Steer.REG_STR_RESUME
REG_STR_DETACH = _ReG_Steer.REG_STR_DETACH
REG_STR_EMIT_PARAM_LOG = _ReG_Steer.REG_STR_EMIT_PARAM_LOG
REG_STR_PAUSE_INTERNAL = _ReG_Steer.REG_STR_PAUSE_INTERNAL
REG_MIN_IOTYPE_HANDLE = _ReG_Steer.REG_MIN_IOTYPE_HANDLE
REG_IO_IN = _ReG_Steer.REG_IO_IN
REG_IO_OUT = _ReG_Steer.REG_IO_OUT
REG_IO_INOUT = _ReG_Steer.REG_IO_INOUT
REG_IO_BUFSIZE = _ReG_Steer.REG_IO_BUFSIZE
REG_PACKET_SIZE = _ReG_Steer.REG_PACKET_SIZE
REG_PACKET_FORMAT = _ReG_Steer.REG_PACKET_FORMAT
REG_DATA_HEADER = _ReG_Steer.REG_DATA_HEADER
REG_DATA_FOOTER = _ReG_Steer.REG_DATA_FOOTER
BEGIN_SLICE_HEADER = _ReG_Steer.BEGIN_SLICE_HEADER
END_SLICE_HEADER = _ReG_Steer.END_SLICE_HEADER
REG_INT = _ReG_Steer.REG_INT
REG_FLOAT = _ReG_Steer.REG_FLOAT
REG_DBL = _ReG_Steer.REG_DBL
REG_CHAR = _ReG_Steer.REG_CHAR
REG_XDR_INT = _ReG_Steer.REG_XDR_INT
REG_XDR_FLOAT = _ReG_Steer.REG_XDR_FLOAT
REG_XDR_DOUBLE = _ReG_Steer.REG_XDR_DOUBLE
REG_BIN = _ReG_Steer.REG_BIN
REG_LONG = _ReG_Steer.REG_LONG
REG_XDR_LONG = _ReG_Steer.REG_XDR_LONG
REG_SIZEOF_XDR_INT = _ReG_Steer.REG_SIZEOF_XDR_INT
REG_SIZEOF_XDR_FLOAT = _ReG_Steer.REG_SIZEOF_XDR_FLOAT
REG_SIZEOF_XDR_DOUBLE = _ReG_Steer.REG_SIZEOF_XDR_DOUBLE
REG_MAX_SIZEOF_XDR_TYPE = _ReG_Steer.REG_MAX_SIZEOF_XDR_TYPE
REG_FALSE = _ReG_Steer.REG_FALSE
REG_TRUE = _ReG_Steer.REG_TRUE
reg_false = _ReG_Steer.reg_false
reg_true = _ReG_Steer.reg_true
REG_SIM_HANDLE_NOTSET = _ReG_Steer.REG_SIM_HANDLE_NOTSET
REG_IODEF_HANDLE_NOTSET = _ReG_Steer.REG_IODEF_HANDLE_NOTSET
REG_PARAM_HANDLE_NOTSET = _ReG_Steer.REG_PARAM_HANDLE_NOTSET
REG_SEQ_NUM_HANDLE = _ReG_Steer.REG_SEQ_NUM_HANDLE
REG_STEP_TIME_HANDLE = _ReG_Steer.REG_STEP_TIME_HANDLE
REG_TIMESTAMP_HANDLE = _ReG_Steer.REG_TIMESTAMP_HANDLE
REG_STEER_INTERVAL_HANDLE = _ReG_Steer.REG_STEER_INTERVAL_HANDLE
REG_TOT_SIM_TIME_HANDLE = _ReG_Steer.REG_TOT_SIM_TIME_HANDLE
REG_MIN_PARAM_HANDLE = _ReG_Steer.REG_MIN_PARAM_HANDLE
REG_TIMESTEP_LABEL = _ReG_Steer.REG_TIMESTEP_LABEL
MSG_ERROR = _ReG_Steer.MSG_ERROR
MSG_NOTSET = _ReG_Steer.MSG_NOTSET
SUPP_CMDS = _ReG_Steer.SUPP_CMDS
IO_DEFS = _ReG_Steer.IO_DEFS
PARAM_DEFS = _ReG_Steer.PARAM_DEFS
STATUS = _ReG_Steer.STATUS
CONTROL = _ReG_Steer.CONTROL
CHK_DEFS = _ReG_Steer.CHK_DEFS
STEER_LOG = _ReG_Steer.STEER_LOG
REG_MAX_LINE_LEN = _ReG_Steer.REG_MAX_LINE_LEN
REG_PIPE_UNSET = _ReG_Steer.REG_PIPE_UNSET
REG_MAX_MSG_SIZE = _ReG_Steer.REG_MAX_MSG_SIZE
REG_TOL_ZERO = _ReG_Steer.REG_TOL_ZERO
REG_INITIAL_CHK_LOG_SIZE = _ReG_Steer.REG_INITIAL_CHK_LOG_SIZE
REG_LOG_FILENAME = _ReG_Steer.REG_LOG_FILENAME
REG_PARAM_LOG_FILENAME = _ReG_Steer.REG_PARAM_LOG_FILENAME
REG_COMMS_STATUS_NULL = _ReG_Steer.REG_COMMS_STATUS_NULL
REG_COMMS_STATUS_LISTENING = _ReG_Steer.REG_COMMS_STATUS_LISTENING
REG_COMMS_STATUS_WAITING_FOR_ACCEPT = _ReG_Steer.REG_COMMS_STATUS_WAITING_FOR_ACCEPT
REG_COMMS_STATUS_WAITING_TO_CONNECT = _ReG_Steer.REG_COMMS_STATUS_WAITING_TO_CONNECT
REG_COMMS_STATUS_CONNECTED = _ReG_Steer.REG_COMMS_STATUS_CONNECTED
REG_COMMS_STATUS_FAILURE = _ReG_Steer.REG_COMMS_STATUS_FAILURE
REG_COMMS_STATUS_CLOSING = _ReG_Steer.REG_COMMS_STATUS_CLOSING
REG_COMMS_NOT_READY = _ReG_Steer.REG_COMMS_NOT_READY
REG_COMMS_READY_READ = _ReG_Steer.REG_COMMS_READY_READ
REG_COMMS_READY_WRITE = _ReG_Steer.REG_COMMS_READY_WRITE
REG_PORT_NOTSET = _ReG_Steer.REG_PORT_NOTSET
REG_MAX_NUM_SGS_SDE = _ReG_Steer.REG_MAX_NUM_SGS_SDE
REG_SGS_ERROR = _ReG_Steer.REG_SGS_ERROR
REG_SGS_SUCCESS = _ReG_Steer.REG_SGS_SUCCESS
REG_SGS_TIMEOUT = _ReG_Steer.REG_SGS_TIMEOUT
REG_APP_POLL_INTERVAL_DEFAULT = _ReG_Steer.REG_APP_POLL_INTERVAL_DEFAULT
REG_SCRATCH_BUFFER_SIZE = _ReG_Steer.REG_SCRATCH_BUFFER_SIZE
REG_UID_HISTORY_BUFFER_SIZE = _ReG_Steer.REG_UID_HISTORY_BUFFER_SIZE
class intp(_object):
    __swig_setmethods__ = {}
    __setattr__ = lambda self, name, value: _swig_setattr(self, intp, name, value)
    __swig_getmethods__ = {}
    __getattr__ = lambda self, name: _swig_getattr(self, intp, name)
    def __repr__(self):
        return "<%s.%s; proxy of C intp instance at %s>" % (self.__class__.__module__, self.__class__.__name__, self.this,)
    def __init__(self, *args):
        _swig_setattr(self, intp, 'this', _ReG_Steer.new_intp(*args))
        _swig_setattr(self, intp, 'thisown', 1)
    def __del__(self, destroy=_ReG_Steer.delete_intp):
        try:
            if self.thisown: destroy(self)
        except: pass

    def assign(*args): return _ReG_Steer.intp_assign(*args)
    def value(*args): return _ReG_Steer.intp_value(*args)
    def cast(*args): return _ReG_Steer.intp_cast(*args)
    __swig_getmethods__["frompointer"] = lambda x: _ReG_Steer.intp_frompointer
    if _newclass:frompointer = staticmethod(_ReG_Steer.intp_frompointer)

class intpPtr(intp):
    def __init__(self, this):
        _swig_setattr(self, intp, 'this', this)
        if not hasattr(self,"thisown"): _swig_setattr(self, intp, 'thisown', 0)
        self.__class__ = intp
_ReG_Steer.intp_swigregister(intpPtr)

intp_frompointer = _ReG_Steer.intp_frompointer

class floatp(_object):
    __swig_setmethods__ = {}
    __setattr__ = lambda self, name, value: _swig_setattr(self, floatp, name, value)
    __swig_getmethods__ = {}
    __getattr__ = lambda self, name: _swig_getattr(self, floatp, name)
    def __repr__(self):
        return "<%s.%s; proxy of C floatp instance at %s>" % (self.__class__.__module__, self.__class__.__name__, self.this,)
    def __init__(self, *args):
        _swig_setattr(self, floatp, 'this', _ReG_Steer.new_floatp(*args))
        _swig_setattr(self, floatp, 'thisown', 1)
    def __del__(self, destroy=_ReG_Steer.delete_floatp):
        try:
            if self.thisown: destroy(self)
        except: pass

    def assign(*args): return _ReG_Steer.floatp_assign(*args)
    def value(*args): return _ReG_Steer.floatp_value(*args)
    def cast(*args): return _ReG_Steer.floatp_cast(*args)
    __swig_getmethods__["frompointer"] = lambda x: _ReG_Steer.floatp_frompointer
    if _newclass:frompointer = staticmethod(_ReG_Steer.floatp_frompointer)

class floatpPtr(floatp):
    def __init__(self, this):
        _swig_setattr(self, floatp, 'this', this)
        if not hasattr(self,"thisown"): _swig_setattr(self, floatp, 'thisown', 0)
        self.__class__ = floatp
_ReG_Steer.floatp_swigregister(floatpPtr)

floatp_frompointer = _ReG_Steer.floatp_frompointer

class doublep(_object):
    __swig_setmethods__ = {}
    __setattr__ = lambda self, name, value: _swig_setattr(self, doublep, name, value)
    __swig_getmethods__ = {}
    __getattr__ = lambda self, name: _swig_getattr(self, doublep, name)
    def __repr__(self):
        return "<%s.%s; proxy of C doublep instance at %s>" % (self.__class__.__module__, self.__class__.__name__, self.this,)
    def __init__(self, *args):
        _swig_setattr(self, doublep, 'this', _ReG_Steer.new_doublep(*args))
        _swig_setattr(self, doublep, 'thisown', 1)
    def __del__(self, destroy=_ReG_Steer.delete_doublep):
        try:
            if self.thisown: destroy(self)
        except: pass

    def assign(*args): return _ReG_Steer.doublep_assign(*args)
    def value(*args): return _ReG_Steer.doublep_value(*args)
    def cast(*args): return _ReG_Steer.doublep_cast(*args)
    __swig_getmethods__["frompointer"] = lambda x: _ReG_Steer.doublep_frompointer
    if _newclass:frompointer = staticmethod(_ReG_Steer.doublep_frompointer)

class doublepPtr(doublep):
    def __init__(self, this):
        _swig_setattr(self, doublep, 'this', this)
        if not hasattr(self,"thisown"): _swig_setattr(self, doublep, 'thisown', 0)
        self.__class__ = doublep
_ReG_Steer.doublep_swigregister(doublepPtr)

doublep_frompointer = _ReG_Steer.doublep_frompointer

REG_DEBUG = _ReG_Steer.REG_DEBUG

Steering_enable = _ReG_Steer.Steering_enable

Steering_initialize = _ReG_Steer.Steering_initialize

Register_IOType = _ReG_Steer.Register_IOType

Enable_IOTypes_on_registration = _ReG_Steer.Enable_IOTypes_on_registration

Disable_IOType = _ReG_Steer.Disable_IOType

Enable_IOType = _ReG_Steer.Enable_IOType

Enable_IOType_acks = _ReG_Steer.Enable_IOType_acks

Disable_IOType_acks = _ReG_Steer.Disable_IOType_acks

Register_param = _ReG_Steer.Register_param

Steering_control = _ReG_Steer.Steering_control

Consume_start = _ReG_Steer.Consume_start

Consume_start_blocking = _ReG_Steer.Consume_start_blocking

Consume_data_slice_header = _ReG_Steer.Consume_data_slice_header

Consume_data_slice = _ReG_Steer.Consume_data_slice

Consume_stop = _ReG_Steer.Consume_stop

Steering_finalize = _ReG_Steer.Steering_finalize

Sizeof = _ReG_Steer.Sizeof


