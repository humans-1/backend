package org.example.humans.domain.note.exception;

import org.example.humans.global.apiPayload.code.BaseErrorCode;
import org.example.humans.global.apiPayload.exception.GeneralException;

public class NoteException extends GeneralException {
    public NoteException(BaseErrorCode code){
        super(code);
    }
}
