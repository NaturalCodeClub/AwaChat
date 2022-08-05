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
    std::vector<QString> frineds;
private:
    Ui::MainWindow *ui;
    QTcpSocket *socket;
    bool result_login=false;
    QString user;
    std::map<QString,std::vector<Message*>>history;
    QString now_target;
    QSoundEffect *soundEffect;
    int last_length;
    QByteArray data_buffer;
    void WriteToServer(QByteArray bf);
    int HandleReveData(QByteArray &data);
};
#endif // MAINWINDOW_H
