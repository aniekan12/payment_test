import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel('flutterwave.irecharge/payments');

  String _flutterwave = 'Unknown flutterwave.';

  Future<void> _processTransaction() async {
    String flutterwave;
    try {
      var result = await platform.invokeMethod('processTransactions');
      flutterwave = 'process transactions $result % .';
    } on PlatformException catch (e) {
      flutterwave = "Failed to process transaction: '${e.message}'.";
    }

    setState(() {
      _flutterwave = flutterwave;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            TextButton(
                onPressed: _processTransaction,
                child: Text('Process Transaction')),
          ],
        ),
      ),
    );
  }
}
