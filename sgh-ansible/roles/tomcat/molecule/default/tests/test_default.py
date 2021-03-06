import os

import testinfra.utils.ansible_runner

testinfra_hosts = testinfra.utils.ansible_runner.AnsibleRunner(
    os.environ['MOLECULE_INVENTORY_FILE']
).get_hosts('all')


def test_hosts_file(host):
    f = host.file('/etc/hosts')
    tomcat = host.service("tomcat")    
    assert tomcat.is_running    
    assert tomcat.is_enabled

    assert f.exists
    assert f.user == 'root'
    assert f.group == 'root'
