#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QtNetwork/QTcpSocket>

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
    ~MainWindow();

private:
    Ui::MainWindow *ui;
    QTcpSocket *socket;
    bool result_login;
};
#endif // MAINWINDOW_H
