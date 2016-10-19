var callback = arguments[arguments.length - 1];
function groupsLoaded() {
    var groups = Ext.getCmp('group-combobox');
    if (!groups || (groups.getStore().count() <= 0)) {
        setTimeout(groupsLoaded, 1000);
    } else {
        callback();
    }
}
groupsLoaded();
