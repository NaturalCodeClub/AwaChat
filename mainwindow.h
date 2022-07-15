#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QtNetwork/QTcpSocket>
#include <QSoundEffect>
#include <vector>
#include <map>
#include "Message.h"

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    bool login(QString user,QString pwd);
    void ReadData();
    void SelectedFriendTarget(int row);
    void OnButSend();
    ~MainWindow();

private:
    Ui::MainWindow *ui;
    QTcpSocket *socket;
    bool result_login;
    QString user;
    std::vector<QString> frineds;
    std::map<QString,std::vector<Message*>>history;
    //std::vector<Activity*>activities;
    QString now_target;
    QSoundEffect *soundEffect;
};
#endif // MAINWINDOW_H
