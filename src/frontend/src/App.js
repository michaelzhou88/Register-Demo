import React,{useState, useEffect} from 'react';
import {deleteEmployee, getAllEmployees} from "./client";
import {
    Layout,
    Menu,
    Breadcrumb,
    Table,
    Spin,
    Empty,
    Button,
    Badge,
    Tag,
    Avatar,
    Radio,
    Popconfirm
} from 'antd';
import {
    DesktopOutlined,
    PieChartOutlined,
    FileOutlined,
    TeamOutlined,
    UserOutlined,
    LoadingOutlined,
    PlusOutlined
} from '@ant-design/icons';
import EmployeeDrawerForm from "./EmployeeDrawerForm";

import './App.css';
import {successNotification, errorNotification} from "./Notification";

const {Header, Content, Footer, Sider} = Layout;
const {SubMenu} = Menu;

const TheAvatar = ({name}) => {
    let trim = name.trim();
    if (trim.length === 0) {
        return <Avatar icon={<UserOutlined/>} />
    }
    const split = trim.split(" ");
    if (split.length === 1) {
        return <Avatar>{name.charAt(0)}</Avatar>
    }
    return <Avatar>
            {`${name.charAt(0)}${name.charAt(name.length-1)}`}
            </Avatar>
}

const removeEmployee = (employeeId, callback) => {
    deleteEmployee(employeeId).then(() => {
        successNotification( "Employee deleted", `Employee ID: ${employeeId} was deleted`);
        callback();
    }).catch(err => {
        err.response.json().then(res => {
            console.log(res);
            errorNotification("There was an issue", `${res.message} [${res.status}] [${res.error}]`)
        })
    });
}

const columns = fetchEmployees => [
    {
        title: '',
        dataIndex: 'avatar',
        key: 'avatar',
        render: (text, employee) =>
            <TheAvatar name={employee.name}/>
    },
    {
        title: 'Id',
        dataIndex: 'id',
        key: 'id',
    },
    {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
    },
    {
        title: 'Email',
        dataIndex: 'email',
        key: 'email',
    },
    {
        title: 'Gender',
        dataIndex: 'gender',
        key: 'gender',
    },
    {
        title: 'Actions',
        key: 'actions',
        render: (text, employee) =>
            <Radio.Group>
                    <Popconfirm
                        placement='topRight'
                        title={`Are you sure to delete ${employee.name}`}
                        onConfirm={() => removeEmployee(employee.id, fetchEmployees)}
                        okText='Yes'
                        cancelText='No'>
                        <Radio.Button value="small">Delete</Radio.Button>
                    </Popconfirm>
                    <Radio.Button value="small">Edit</Radio.Button>
            </Radio.Group>
    }
];

const antIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;

function App() {
    const [employees, setEmployees] = useState([]);
    const [collapsed, setCollapsed] = useState(false);
    const [fetching, setFetching] = useState(true);
    const [showDrawer, setShowDrawer] = useState(false);

    const fetchEmployees = () =>
        getAllEmployees()
            .then(res => res.json())
            .then(data => {
                console.log(data);
                setEmployees(data);
            }).catch(err => {
                console.log(err.response)
                err.response.json().then(res => {
                    console.log(res)
                    errorNotification("There was an issue", `${res.message} [${res.status}] [${res.error}]`, "bottomLeft")
                    });
            }).finally(() => {
                setFetching(false);
            });

    useEffect(() => {
        console.log("Component is mounted");
        fetchEmployees();
    }, []);

    const renderEmployees = () => {
        if (fetching) {
            return <Spin indicator={antIcon} />
        }
        if (employees.length <= 0) {
            return <>
                <Button
                     onClick={() => setShowDrawer(!showDrawer)}
                     type="primary" shape="round" icon={<PlusOutlined />} size="small">
                     Add New Employee
                </Button>
                <EmployeeDrawerForm
                    showDrawer={showDrawer}
                    setShowDrawer={setShowDrawer}
                    fetchEmployees={fetchEmployees}
                />
                <Empty />
            </>
        }
        return <>
             <EmployeeDrawerForm
                showDrawer={showDrawer}
                setShowDrawer={setShowDrawer}
                fetchEmployees={fetchEmployees}
             />
            <Table
                dataSource={employees}
                columns={columns(fetchEmployees)}
                bordered
                title={() =>
                <>
                    <Tag>Number of employees</Tag>
                    <Badge style={{marginLeft: "5px"}} count={employees.length} className="site-badge-count-4"/>
                    <br />
                    <br />
                    <Button
                         onClick={() => setShowDrawer(!showDrawer)}
                         type="primary" shape="round" icon={<PlusOutlined />} size="small">
                         Add New Employee
                    </Button>
                </>
            }
                pagination={{ pageSize: 50 }}
                scroll={{ y: 500 }}
                rowKey={employee => employee.id}
            />;
        </>
    }

    return <Layout style={{minHeight: '100vh'}}>
        <Sider collapsible collapsed={collapsed}
               onCollapse={setCollapsed}>
            <div className="logo"/>
            <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
                <Menu.Item key="1" icon={<PieChartOutlined/>}>
                    Option 1
                </Menu.Item>
                <Menu.Item key="2" icon={<DesktopOutlined/>}>
                    Option 2
                </Menu.Item>
                <SubMenu key="sub1" icon={<UserOutlined/>} title="User">
                    <Menu.Item key="3">Tom</Menu.Item>
                    <Menu.Item key="4">Bill</Menu.Item>
                    <Menu.Item key="5">Alex</Menu.Item>
                </SubMenu>
                <SubMenu key="sub2" icon={<TeamOutlined/>} title="Team">
                    <Menu.Item key="6">Team 1</Menu.Item>
                    <Menu.Item key="8">Team 2</Menu.Item>
                </SubMenu>
                <Menu.Item key="9" icon={<FileOutlined/>}>
                    Files
                </Menu.Item>
            </Menu>
        </Sider>
        <Layout className="site-layout">
            <Header className="site-layout-background" style={{padding: 0}}/>
            <Content style={{margin: '0 16px'}}>
                <Breadcrumb style={{margin: '16px 0'}}>
                    <Breadcrumb.Item>User</Breadcrumb.Item>
                    <Breadcrumb.Item>Bill</Breadcrumb.Item>
                </Breadcrumb>
                <div className="site-layout-background" style={{padding: 24, minHeight: 360}}>
                    {renderEmployees()}
                </div>
            </Content>
            <Footer style={{textAlign: 'center'}}>By Michael Zhou</Footer>
        </Layout>
    </Layout>
}

export default App;