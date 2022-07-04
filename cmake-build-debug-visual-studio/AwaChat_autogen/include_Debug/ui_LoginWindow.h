/********************************************************************************
** Form generated from reading UI file 'loginwindow.ui'
**
** Created by: Qt User Interface Compiler version 6.1.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_LOGINWINDOW_H
#define UI_LOGINWINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_LoginWindow
{
public:
    QLineEdit *edit_user;
    QLineEdit *edit_pwd;
    QPushButton *but_login;

    void setupUi(QWidget *LoginWindow)
    {
        if (LoginWindow->objectName().isEmpty())
            LoginWindow->setObjectName(QString::fromUtf8("LoginWindow"));
        LoginWindow->resize(350, 374);
        LoginWindow->setMinimumSize(QSize(350, 374));
        LoginWindow->setMaximumSize(QSize(350, 374));
        edit_user = new QLineEdit(LoginWindow);
        edit_user->setObjectName(QString::fromUtf8("edit_user"));
        edit_user->setGeometry(QRect(70, 110, 200, 40));
        edit_user->setStyleSheet(QString::fromUtf8("QLineEdit{\n"
"background-color: rgb(240, 240, 240);\n"
"border-color: rgb(99, 99, 99);\n"
"border-style: solid;\n"
"border-top-width: 0px;\n"
"border-left-width: 0px;\n"
"border-right-width: 0px;\n"
"border-bottom-width: 1px;\n"
"}\n"
"QLineEdit:focus{\n"
"border-color: rgb(109, 109, 109);\n"
"}"));
        edit_pwd = new QLineEdit(LoginWindow);
        edit_pwd->setObjectName(QString::fromUtf8("edit_pwd"));
        edit_pwd->setGeometry(QRect(70, 180, 200, 40));
        edit_pwd->setStyleSheet(QString::fromUtf8("QLineEdit{\n"
"background-color: rgb(240, 240, 240);\n"
"border-color: rgb(99, 99, 99);\n"
"border-style: solid;\n"
"border-top-width: 0px;\n"
"border-left-width: 0px;\n"
"border-right-width: 0px;\n"
"border-bottom-width: 1px;\n"
"}\n"
"QLineEdit:focus{\n"
"border-color: rgb(109, 109, 109);\n"
"}"));
        edit_pwd->setEchoMode(QLineEdit::Password);
        but_login = new QPushButton(LoginWindow);
        but_login->setObjectName(QString::fromUtf8("but_login"));
        but_login->setEnabled(true);
        but_login->setGeometry(QRect(70, 250, 201, 51));
        but_login->setStyleSheet(QString::fromUtf8("QPushButton{\n"
"border:2px;\n"
"background-color: rgb(61, 197, 255);\n"
"background-radius: 10px;\n"
"border-radius: 3px;\n"
"}\n"
"\n"
"QPushButton:hover{\n"
"background-color: rgb(49, 160, 204);\n"
"}\n"
"QPushButton:pressed{\n"
"background-color: rgb(71, 227, 244);\n"
"}\n"
"QPushButton:disabled{\n"
"background-color:	rgb(99, 99, 99)\n"
"}"));

        retranslateUi(LoginWindow);

        QMetaObject::connectSlotsByName(LoginWindow);
    } // setupUi

    void retranslateUi(QWidget *LoginWindow)
    {
        LoginWindow->setWindowTitle(QCoreApplication::translate("LoginWindow", "LoginWindow", nullptr));
        but_login->setText(QCoreApplication::translate("LoginWindow", "\347\231\273\345\275\225", nullptr));
    } // retranslateUi

};

namespace Ui {
    class LoginWindow: public Ui_LoginWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_LOGINWINDOW_H
