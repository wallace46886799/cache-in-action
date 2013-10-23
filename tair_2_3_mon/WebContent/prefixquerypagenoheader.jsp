<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Prefix Query</title>

<link rel="stylesheet" type="text/css"
	href="./resources/css/ext-all.css" />
<style type="text/css">
</style>

<!-- GC -->
<!-- LIBS -->
<script type="text/javascript" src="./adapter/ext/ext-base.js"></script>
<!-- ENDLIBS -->

<script type="text/javascript" src="./ext-all.js"></script>

<!-- extensions -->

</head>
<body>
<center>

<script type="text/javascript">
		Ext.onReady(function() {
			
			Ext.QuickTips.init();

		    // turn on validation errors beside the field globally
		    Ext.form.Field.prototype.msgTarget = 'side';
		    
			    var fs = new Ext.FormPanel({
			        frame: true,
			        title:'C++ Prefix Query',
			        labelAlign: 'right',
			        labelWidth: 120,
			        width:1024,
			        waitMsgTarget: true,

			        // configure how to read the XML Data
			        reader : new Ext.data.JsonReader( 
		        		{                     
		        			totalProperty: "totalProperty",  //totalRecords������json.results�õ�                     
		        			successProperty: true,    //json�����У������Ƿ񷵻سɹ���������                     
		        			root: "root"           //����Ԫ���ݵ�������json.rows�õ�                               
		        		}, 	
        			[{ name: 'Area' },       //���name��mappingͬ��,����ʡ��mapping  
        		   	 { name: 'Pkey' },
        		   	 { name: 'Skey' },
        		   	 { name: 'Value' }] 
		             ),

			        items: [
			            new Ext.form.FieldSet({
			                title: 'Contact Information',
			                autoHeight: true,
			                defaultType: 'textfield',
			                items: [ {
			                        	fieldLabel: 'Area',
			                        	emptyText: '',
			                        	name: 'Area',
			                       	 	width:830
			                    	},	{
			                        	fieldLabel: 'Pkey',
			                        	emptyText: 'Pkey',
			                        	name: 'Pkey',
			                        	width:830
			                    	},	{
			                        	fieldLabel: 'Skey',
			                        	emptyText: 'Skey',
			                        	name: 'Skey',
			                        	width:830
			                    	},	{
				                        fieldLabel: 'Value',
				                        emptyText: '',
				                        //disabled:true,
				                        name: 'Value',
				                        width:830
			                    	}
			                ]
			            })
			        ]
			    });

			    // simple button add
			    fs.addButton('Load', function(){
			        fs.getForm().load({
			        	url:'prefixgetrequestnoheader?Area='+fs.getForm().findField('Area').getValue()+
			        			'&Pkey='+escape(escape(fs.getForm().findField('Pkey').getValue())) +
			        			'&Skey='+escape(escape(fs.getForm().findField('Skey').getValue())), 
			        	waitMsg:'Loading'});
			    });
	
			    fs.render('form-ct');

	    
			 
		});
	</script>
<jsp:include page="__common_head.jsp"></jsp:include>
<div id="form-ct"></div> 
</center>
</body>
</html>