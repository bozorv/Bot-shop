package org.example.Button;

import static org.example.Button.ButtonName.*;

public interface ButtonArray {
    String[][] userMenu = {
            {EXIT, SAVAT},
            {QIZ_BOLA, UGIL_BOLA},
            {MAYKALAR, NEW_GOODS},
            {SALES_GOODS}
    };
    String[][] blocUser = {
            {CHAT_WITH_ADMIN,EXIT}
    };
    String[][] firstMenu = {
            {ORDER_GOODS},
            {SAVAT, CHAT_WITH_ADMIN}
    };

    String[][] adminMarkup = {
            {ADD_GOODS, DELETE_GOODS},
            {ACTIVE_USERS}
    };
    String[][] exit = {
            {EXIT, SAVAT}
    };
    String[][] exitadmin = {
            {EXIT}
    };
    String[][] womens = {
            {EXIT, SAVAT},
            {QIMMAT, ARZON}
    };
    String[][] detski = {
            {EXIT, SAVAT},
            {UGIL_BOLA, QIZ_BOLA}
    };


    String[][] adminMenu = {
            {ADD_QIMMAT, ADD_ARZON},
            {ADD_MENS, ADD_MAYKALAR},
            {ADD_UGIL_BOLA, ADD_QIZ_BOLA},
            {ADD_ALL_WOMEN_GOODS, ADD_NEW_GOODS},
            {ADD_SALES_GOODS,EXIT}
    };
    String[][] adminDeleteMenu = {
            {DELETE_QIMMAT, DELETE_ARZON},
            {DELETE_MENS,  DELETE_MAYKALAR},
            {DELETE_UGIL_BOLA, DELETE_QIZ_BOLA},
            { DELETE_NEW_GOODS,DELETE_SALES_GOODS},
            {EXIT}
    };

}