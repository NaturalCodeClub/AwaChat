//
// Created by Ingil Ying on 2022/7/3.
//

// You may need to build the project (run Qt uic code generator) to get "ui_LoginWindow.h" resolved

#include "loginwindow.h"
#include "ui_LoginWindow.h"
#include <QAction>


LoginWindow::LoginWindow(QWidget *parent) :
        QWidget(parent), ui(new Ui::LoginWindow) {
    ui->setupUi(this);
    QAction *userAction=new QAction(ui->edit_user);
    userAction->setIcon(QIcon("://images/icon_user.png"));
    ui->edit_user->addAction(userAction,QLineEdit::LeadingPosition);
    QAction *pwdAction=new QAction(ui->edit_pwd);
    pwdAction->setIcon(QIcon("://images/icon_pwd.png"));
    ui->edit_pwd->addAction(pwdAction,QLineEdit::LeadingPosition);
    connect(ui->but_login,&QPushButton::clicked,this,&LoginWindow::OnButLoginClicked);
    mainWindow=new MainWindow();

}

void LoginWindow::OnButLoginClicked()
{
    mainWindow->show();
    return;
    ui->but_login->setDisabled(true);
    if(mainWindow->login(ui->edit_user->text(),ui->edit_pwd->text()))
    {
        mainWindow->show();
        this->hide();
    }else ui->but_login->setDisabled(false);
}

LoginWindow::~LoginWindow() {
    delete ui;
}
