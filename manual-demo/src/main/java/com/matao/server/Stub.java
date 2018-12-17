package com.matao.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import com.matao.Book;

import java.util.List;

/**
 * Created by matao on 2018/12/17
 */
public abstract class Stub extends Binder implements IBookManager {

    public static final int TRANSACTION_ADD_BOOK = IBinder.FIRST_CALL_TRANSACTION;
    public static final int TRANSACTION_GET_BOOKS = IBinder.FIRST_CALL_TRANSACTION + 1;

    public Stub() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder binder) {
        if (binder == null) return null;

        IInterface iInterface = binder.queryLocalInterface(DESCRIPTOR);
        if (iInterface instanceof IBookManager) {
            return (IBookManager) iInterface;
        }
        return new Proxy(binder);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
            case TRANSACTION_ADD_BOOK:
                data.enforceInterface(DESCRIPTOR);
                Book book = null;
                if (data.readInt() != 0) {
                    book = Book.CREATOR.createFromParcel(data);
                }
                addBook(book);
                reply.writeNoException();
                return true;
            case TRANSACTION_GET_BOOKS:
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBooks();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            default:
                return super.onTransact(code, data, reply, flags);
        }
    }
}
