//
// Created by Ingil Ying on 2022/7/3.
//

#ifndef AWACHAT_LOGINWINDOW_H
#define AWACHAT_LOGINWINDOW_H

#include <QWidget>
#include "mainwindow.h"

QT_BEGIN_NAMESPACE
namespace Ui { class LoginWindow; }
QT_END_NAMESPACE

class LoginWindow : public QWidget {
Q_OBJECT

public:
    explicit LoginWindow(QWidget *parent = nullptr);
    void OnButLoginClicked();
    ~LoginWindow() override;

private:
    Ui::LoginWindow *ui;
    MainWindow *mainWindow;
};


#endif //AWACHAT_LOGINWINDOW_H
